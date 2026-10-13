package com.dsa.learning.execution;

import com.dsa.learning.dto.ExecutionResultDto;
import com.dsa.learning.dto.TestCaseResultDto;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.SubmissionStatus;
import com.dsa.learning.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

@Component
public class JavaCodeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JavaCodeExecutor.class);

    @Value("${executor.timeout.seconds:3}")
    private int timeoutSeconds;

    public ExecutionResultDto execute(Problem problem, String studentCode, List<TestCase> testCases) {
        ExecutionResultDto result = new ExecutionResultDto();
        result.setTotalCount(testCases.size());

        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("dsa_exec_");
            File tempFolder = tempDir.toFile();

            // 1. Write student code
            String cleanedCode = sanitizeCode(studentCode);
            File solutionFile = new File(tempFolder, "Solution.java");
            Files.writeString(solutionFile.toPath(), cleanedCode, StandardCharsets.UTF_8);

            // 2. Generate Harness Driver.java based on Problem signature and test cases
            String driverCode = generateDriverCode(problem, testCases);
            File driverFile = new File(tempFolder, "Driver.java");
            Files.writeString(driverFile.toPath(), driverCode, StandardCharsets.UTF_8);

            // 3. Compile using JavaCompiler / javac
            String compileError = compileCode(tempFolder, solutionFile, driverFile);
            if (compileError != null && !compileError.trim().isEmpty()) {
                result.setStatus(SubmissionStatus.COMPILATION_ERROR);
                result.setCompilerError(formatCompilerError(compileError));
                return result;
            }

            // 4. Execute Driver.java
            return runDriverProcess(tempFolder, testCases);

        } catch (Exception e) {
            logger.error("Error during code execution", e);
            result.setStatus(SubmissionStatus.RUNTIME_ERROR);
            result.setCompilerError("Internal execution failure: " + e.getMessage());
            return result;
        } finally {
            cleanupTempDir(tempDir);
        }
    }

    private String sanitizeCode(String code) {
        if (code == null) return "";
        // Ensure necessary common imports exist if not present
        StringBuilder sb = new StringBuilder();
        if (!code.contains("package ")) {
            sb.append("import java.util.*;\n");
            sb.append("import java.io.*;\n");
            sb.append("import java.math.*;\n");
        }
        sb.append(code);
        return sb.toString();
    }

    private String compileCode(File tempFolder, File solutionFile, File driverFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler != null) {
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            int compResult = compiler.run(null, null, errStream,
                    "-d", tempFolder.getAbsolutePath(),
                    solutionFile.getAbsolutePath(),
                    driverFile.getAbsolutePath()
            );
            if (compResult != 0) {
                return new String(errStream.toByteArray(), StandardCharsets.UTF_8);
            }
            return null;
        } else {
            // Fallback to process javac
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "javac",
                        "-d", tempFolder.getAbsolutePath(),
                        solutionFile.getAbsolutePath(),
                        driverFile.getAbsolutePath()
                );
                pb.redirectErrorStream(true);
                Process process = pb.start();
                String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    return output;
                }
                return null;
            } catch (Exception e) {
                return "Failed to invoke javac: " + e.getMessage();
            }
        }
    }

    private ExecutionResultDto runDriverProcess(File tempFolder, List<TestCase> testCases) {
        ExecutionResultDto result = new ExecutionResultDto();
        result.setTotalCount(testCases.size());
        List<TestCaseResultDto> testResults = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", tempFolder.getAbsolutePath(), "Driver");
            pb.redirectErrorStream(true);
            process = pb.start();

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            long execTime = System.currentTimeMillis() - startTime;
            result.setExecutionTimeMs(execTime);

            if (!finished) {
                process.destroyForcibly();
                result.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
                result.setCompilerError("Time Limit Exceeded (> " + timeoutSeconds + " seconds). Check for infinite loops or non-terminating recursion.");
                return result;
            }

            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.exitValue();

            if (exitCode != 0 && (output.contains("Exception") || output.contains("Error"))) {
                result.setStatus(SubmissionStatus.RUNTIME_ERROR);
                result.setCompilerError(cleanRuntimeOutput(output));
                return result;
            }

            // Parse driver output for test cases
            // Format from Driver: "---TEST_CASE_START---" ... "---TEST_CASE_END---"
            return parseDriverOutput(output, testCases, execTime);

        } catch (InterruptedException e) {
            if (process != null) process.destroyForcibly();
            result.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
            result.setCompilerError("Execution interrupted / Time Limit Exceeded.");
            return result;
        } catch (Exception e) {
            result.setStatus(SubmissionStatus.RUNTIME_ERROR);
            result.setCompilerError("Runtime error: " + e.getMessage());
            return result;
        }
    }

    private ExecutionResultDto parseDriverOutput(String rawOutput, List<TestCase> testCases, long execTime) {
        ExecutionResultDto result = new ExecutionResultDto();
        result.setExecutionTimeMs(execTime);
        result.setTotalCount(testCases.size());
        List<TestCaseResultDto> testResults = new ArrayList<>();

        int passedCount = 0;
        boolean allPassed = true;
        String firstFailedInput = null;
        String firstActualOutput = null;
        String firstExpectedOutput = null;

        String[] lines = rawOutput.split("\\R");
        Map<Integer, String> outputsByIndex = new HashMap<>();

        int currentIndex = -1;
        StringBuilder currentOut = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("---TEST_CASE_START:")) {
                String idxStr = line.substring("---TEST_CASE_START:".length()).replace("---", "").trim();
                try {
                    currentIndex = Integer.parseInt(idxStr);
                    currentOut = new StringBuilder();
                } catch (Exception ignored) {}
            } else if (line.startsWith("---TEST_CASE_END---")) {
                if (currentIndex >= 0) {
                    outputsByIndex.put(currentIndex, currentOut.toString().trim());
                    currentIndex = -1;
                }
            } else {
                if (currentIndex >= 0) {
                    if (currentOut.length() > 0) currentOut.append("\n");
                    currentOut.append(line);
                }
            }
        }

        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            TestCaseResultDto tcRes = new TestCaseResultDto();
            tcRes.setTestIndex(i + 1);
            tcRes.setSample(Boolean.TRUE.equals(tc.getIsSample()));
            tcRes.setHidden(Boolean.TRUE.equals(tc.getIsHidden()));
            tcRes.setInput(tc.getInput());
            tcRes.setExpectedOutput(tc.getExpectedOutput() != null ? tc.getExpectedOutput().trim() : "");

            String actual = outputsByIndex.getOrDefault(i, "").trim();
            tcRes.setActualOutput(actual);

            boolean match = normalizeOutput(actual).equals(normalizeOutput(tc.getExpectedOutput()));
            tcRes.setPassed(match);

            if (match) {
                passedCount++;
            } else {
                allPassed = false;
                if (firstFailedInput == null) {
                    firstFailedInput = tc.getInput();
                    firstActualOutput = actual;
                    firstExpectedOutput = tc.getExpectedOutput();
                }
            }
            testResults.add(tcRes);
        }

        result.setPassedCount(passedCount);
        result.setTestCaseResults(testResults);

        if (allPassed && testCases.size() > 0) {
            result.setStatus(SubmissionStatus.ACCEPTED);
        } else {
            result.setStatus(SubmissionStatus.WRONG_ANSWER);
            result.setCompilerError("Failed on test case. Expected: [" + firstExpectedOutput + "], Got: [" + firstActualOutput + "]");
        }

        return result;
    }

    private String normalizeOutput(String s) {
        if (s == null) return "";
        return s.replaceAll("\\s+", " ").replaceAll("\\[ ", "[").replaceAll(" \\]", "]").replaceAll(", ", ",").trim();
    }

    private String cleanRuntimeOutput(String raw) {
        if (raw == null) return "";
        // Strip out noisy internal stack traces if possible
        String[] lines = raw.split("\\R");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (!line.contains("at Driver.main") && !line.contains("at java.base/")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private String formatCompilerError(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("(?i)[a-z]:\\\\[^\\n]*\\\\Driver\\.java", "Driver.java")
                  .replaceAll("(?i)[a-z]:\\\\[^\\n]*\\\\Solution\\.java", "Solution.java");
    }

    private String generateDriverCode(Problem problem, List<TestCase> testCases) {
        if (problem.getDriverCodeTemplate() != null && !problem.getDriverCodeTemplate().trim().isEmpty()) {
            return problem.getDriverCodeTemplate();
        }

        // Generic dynamic runner generator
        String methodName = problem.getMethodName() != null ? problem.getMethodName() : "solve";
        String returnType = problem.getReturnType() != null ? problem.getReturnType() : "int";
        String paramTypes = problem.getParameterTypes() != null ? problem.getParameterTypes() : "int[]";

        StringBuilder sb = new StringBuilder();
        sb.append("import java.util.*;\n");
        sb.append("import java.io.*;\n\n");
        sb.append("public class Driver {\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        Solution sol = new Solution();\n");

        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            sb.append("        try {\n");
            sb.append("            System.out.println(\"---TEST_CASE_START:").append(i).append("---\");\n");
            sb.append("            String input = \"").append(escapeJava(tc.getInput())).append("\";\n");
            sb.append("            executeCase(sol, input, ").append(i).append(");\n");
            sb.append("            System.out.println(\"---TEST_CASE_END---\");\n");
            sb.append("        } catch (Throwable t) {\n");
            sb.append("            System.out.println(\"Runtime Exception: \" + t.toString());\n");
            sb.append("            System.out.println(\"---TEST_CASE_END---\");\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // Execute Case dispatcher
        sb.append("    private static void executeCase(Solution sol, String input, int index) {\n");
        sb.append(generateCaseInvocation(methodName, returnType, paramTypes));
        sb.append("    }\n");

        // Helpers for array & matrix printing
        sb.append("    private static String formatResult(Object obj) {\n");
        sb.append("        if (obj == null) return \"null\";\n");
        sb.append("        if (obj instanceof int[]) return Arrays.toString((int[]) obj);\n");
        sb.append("        if (obj instanceof long[]) return Arrays.toString((long[]) obj);\n");
        sb.append("        if (obj instanceof double[]) return Arrays.toString((double[]) obj);\n");
        sb.append("        if (obj instanceof boolean[]) return Arrays.toString((boolean[]) obj);\n");
        sb.append("        if (obj instanceof char[]) return new String((char[]) obj);\n");
        sb.append("        if (obj instanceof Object[]) return Arrays.deepToString((Object[]) obj);\n");
        sb.append("        if (obj instanceof List) return obj.toString();\n");
        sb.append("        return String.valueOf(obj);\n");
        sb.append("    }\n");

        // Helper parsers
        sb.append("    private static int[] parseIntArray(String s) {\n");
        sb.append("        s = s.trim();\n");
        sb.append("        if (s.startsWith(\"[\")) s = s.substring(1);\n");
        sb.append("        if (s.endsWith(\"]\")) s = s.substring(0, s.length() - 1);\n");
        sb.append("        if (s.trim().isEmpty()) return new int[0];\n");
        sb.append("        String[] parts = s.split(\",\");\n");
        sb.append("        int[] res = new int[parts.length];\n");
        sb.append("        for (int i = 0; i < parts.length; i++) res[i] = Integer.parseInt(parts[i].trim());\n");
        sb.append("        return res;\n");
        sb.append("    }\n");

        sb.append("    private static String[] parseStringArray(String s) {\n");
        sb.append("        s = s.trim();\n");
        sb.append("        if (s.startsWith(\"[\")) s = s.substring(1);\n");
        sb.append("        if (s.endsWith(\"]\")) s = s.substring(0, s.length() - 1);\n");
        sb.append("        if (s.trim().isEmpty()) return new String[0];\n");
        sb.append("        String[] parts = s.split(\",\");\n");
        sb.append("        String[] res = new String[parts.length];\n");
        sb.append("        for (int i = 0; i < parts.length; i++) {\n");
        sb.append("            String p = parts[i].trim();\n");
        sb.append("            if (p.startsWith(\"\\\"\") && p.endsWith(\"\\\"\") && p.length() >= 2) p = p.substring(1, p.length() - 1);\n");
        sb.append("            res[i] = p;\n");
        sb.append("        }\n");
        sb.append("        return res;\n");
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }

    private String generateCaseInvocation(String methodName, String returnType, String paramTypes) {
        StringBuilder sb = new StringBuilder();
        String[] params = paramTypes.split(";");

        sb.append("        String[] lines = input.split(\"\\\\n\");\n");
        for (int i = 0; i < params.length; i++) {
            String pType = params[i].trim();
            sb.append("        String line").append(i).append(" = (lines.length > ").append(i).append(") ? lines[").append(i).append("].trim() : \"\";\n");

            if ("int[]".equalsIgnoreCase(pType)) {
                sb.append("        int[] arg").append(i).append(" = parseIntArray(line").append(i).append(");\n");
            } else if ("int".equalsIgnoreCase(pType) || "Integer".equalsIgnoreCase(pType)) {
                sb.append("        int arg").append(i).append(" = Integer.parseInt(line").append(i).append(".isEmpty() ? \"0\" : line").append(i).append(");\n");
            } else if ("long".equalsIgnoreCase(pType) || "Long".equalsIgnoreCase(pType)) {
                sb.append("        long arg").append(i).append(" = Long.parseLong(line").append(i).append(".isEmpty() ? \"0\" : line").append(i).append(");\n");
            } else if ("double".equalsIgnoreCase(pType) || "Double".equalsIgnoreCase(pType)) {
                sb.append("        double arg").append(i).append(" = Double.parseDouble(line").append(i).append(".isEmpty() ? \"0.0\" : line").append(i).append(");\n");
            } else if ("boolean".equalsIgnoreCase(pType) || "Boolean".equalsIgnoreCase(pType)) {
                sb.append("        boolean arg").append(i).append(" = Boolean.parseBoolean(line").append(i).append(".isEmpty() ? \"false\" : line").append(i).append(");\n");
            } else if ("String".equalsIgnoreCase(pType)) {
                sb.append("        String arg").append(i).append(" = line").append(i).append(";\n");
            } else if ("String[]".equalsIgnoreCase(pType)) {
                sb.append("        String[] arg").append(i).append(" = parseStringArray(line").append(i).append(");\n");
            } else if ("char[]".equalsIgnoreCase(pType)) {
                sb.append("        char[] arg").append(i).append(" = line").append(i).append(".toCharArray();\n");
            } else {
                sb.append("        String arg").append(i).append(" = line").append(i).append(";\n");
            }
        }

        sb.append("        Object res = sol.").append(methodName).append("(");
        for (int i = 0; i < params.length; i++) {
            sb.append("arg").append(i);
            if (i < params.length - 1) sb.append(", ");
        }
        sb.append(");\n");
        sb.append("        System.out.println(formatResult(res));\n");
        return sb.toString();
    }

    private String escapeJava(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\r", "")
                  .replace("\n", "\\n");
    }

    private void cleanupTempDir(Path tempDir) {
        if (tempDir != null) {
            try {
                Files.walk(tempDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {}
        }
    }
}
