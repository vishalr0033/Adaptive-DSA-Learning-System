package com.dsa.learning.service.ai;

import com.dsa.learning.model.MistakeCategory;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.SubmissionStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAiService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final DeterministicDsaAnalyzer fallbackAnalyzer;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Autowired
    public GeminiAiService(DeterministicDsaAnalyzer fallbackAnalyzer, ObjectMapper objectMapper) {
        this.fallbackAnalyzer = fallbackAnalyzer;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();
    }

    public DeterministicDsaAnalyzer.MistakeAnalysisResult analyzeCodeSubmission(
            Problem problem,
            String submittedCode,
            SubmissionStatus status,
            String compilerError,
            String failedInput,
            String actualOutput,
            String expectedOutput) {

        // Check if Gemini API key is configured
        String effectiveApiKey = getEffectiveApiKey();
        if (effectiveApiKey == null || effectiveApiKey.trim().isEmpty()) {
            logger.info("Gemini API key not provided or empty. Utilizing built-in DSA mistake analyzer.");
            return fallbackAnalyzer.analyze(problem, submittedCode, status, compilerError, failedInput, actualOutput, expectedOutput);
        }

        try {
            String prompt = buildPrompt(problem, submittedCode, status, compilerError, failedInput, actualOutput, expectedOutput);
            String responseJson = callGeminiApi(effectiveApiKey, prompt);
            
            DeterministicDsaAnalyzer.MistakeAnalysisResult aiResult = parseGeminiResponse(responseJson);
            if (aiResult != null) {
                return aiResult;
            }
        } catch (Exception e) {
            logger.warn("Gemini API request failed or timed out. Falling back to local DSA analyzer: {}", e.getMessage());
        }

        // Fallback
        return fallbackAnalyzer.analyze(problem, submittedCode, status, compilerError, failedInput, actualOutput, expectedOutput);
    }

    private String getEffectiveApiKey() {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey.trim();
        }
        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }
        return null;
    }

    private String buildPrompt(Problem problem, String code, SubmissionStatus status, String compilerError, String failedInput, String actualOutput, String expectedOutput) {
        return "You are an expert Data Structures and Algorithms (DSA) educator and code mentor.\n" +
                "Analyze this failed Java code submission for a student.\n\n" +
                "Problem: " + problem.getTitle() + "\n" +
                "DSA Topic: " + problem.getTopic().getDisplayName() + "\n" +
                "Difficulty: " + problem.getDifficulty().name() + "\n" +
                "Problem Description:\n" + problem.getDescription() + "\n\n" +
                "Student Submitted Code:\n```java\n" + code + "\n```\n\n" +
                "Execution Status: " + status.name() + "\n" +
                "Compiler/Runtime Error: " + (compilerError != null ? compilerError : "None") + "\n" +
                "Failed Test Input: " + (failedInput != null ? failedInput : "N/A") + "\n" +
                "Actual Output: " + (actualOutput != null ? actualOutput : "N/A") + "\n" +
                "Expected Output: " + (expectedOutput != null ? expectedOutput : "N/A") + "\n\n" +
                "IMPORTANT INSTRUCTIONS:\n" +
                "1. Choose exactly one Mistake Category from: LOGIC_ERROR, WRONG_ALGORITHM, WRONG_DATA_STRUCTURE, BOUNDARY_CONDITION, EDGE_CASE, LOOP_ERROR, RECURSION_BASE_CASE, POINTER_MOVEMENT, TIME_COMPLEXITY, SPACE_COMPLEXITY.\n" +
                "2. Identify the specific mistake concisely (e.g. 'Incorrect left pointer movement in Sliding Window' or 'Missing base condition for empty subset in Recursion').\n" +
                "3. Explain WHY the student is struggling (do not just say 'you are weak in X'; pinpoint the conceptual misunderstanding).\n" +
                "4. Explain the root cause in the code.\n" +
                "5. Provide a constructive learning suggestion/fix.\n\n" +
                "Respond ONLY with a valid JSON object in this exact format (no markdown fences, raw JSON):\n" +
                "{\n" +
                "  \"category\": \"POINTER_MOVEMENT\",\n" +
                "  \"specificMistake\": \"Incorrect left pointer movement in Sliding Window\",\n" +
                "  \"explanation\": \"The student expands the right boundary but does not shrink the left boundary correctly when a duplicate appears.\",\n" +
                "  \"rootCause\": \"Left pointer remains static inside the while loop instead of incrementing.\",\n" +
                "  \"suggestedFix\": \"When a duplicate is encountered, increment left pointer and remove elements from the set until valid.\"\n" +
                "}";
    }

    private String callGeminiApi(String key, String prompt) throws Exception {
        String endpoint = apiUrl + "?key=" + key;

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBodyMap = Map.of("contents", List.of(parts));

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error code: " + response.statusCode() + " Body: " + response.body());
        }

        return response.body();
    }

    private DeterministicDsaAnalyzer.MistakeAnalysisResult parseGeminiResponse(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    String rawText = parts.get(0).path("text").asText();
                    String cleanJson = rawText.replaceAll("```json", "").replaceAll("```", "").trim();
                    JsonNode resultNode = objectMapper.readTree(cleanJson);

                    String catStr = resultNode.path("category").asText();
                    MistakeCategory category = MistakeCategory.fromString(catStr);
                    String specificMistake = resultNode.path("specificMistake").asText("Logic error in algorithm implementation");
                    String explanation = resultNode.path("explanation").asText("The algorithm failed on specific edge cases.");
                    String rootCause = resultNode.path("rootCause").asText("State transition mismatch.");
                    String suggestedFix = resultNode.path("suggestedFix").asText("Trace through the algorithm with small test inputs.");

                    return new DeterministicDsaAnalyzer.MistakeAnalysisResult(
                            category, specificMistake, explanation, rootCause, suggestedFix
                    );
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse Gemini response JSON: {}", e.getMessage());
        }
        return null;
    }
}
