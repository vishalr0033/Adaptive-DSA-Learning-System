package com.dsa.learning.config;

import com.dsa.learning.model.*;
import com.dsa.learning.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final SubmissionRepository submissionRepository;
    private final MistakeAnalysisRepository mistakeAnalysisRepository;
    private final TopicProgressRepository topicProgressRepository;
    private final RecommendationRepository recommendationRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    public DataInitializer(StudentRepository studentRepository,
                           AdminRepository adminRepository,
                           ProblemRepository problemRepository,
                           TestCaseRepository testCaseRepository,
                           SubmissionRepository submissionRepository,
                           MistakeAnalysisRepository mistakeAnalysisRepository,
                           TopicProgressRepository topicProgressRepository,
                           RecommendationRepository recommendationRepository,
                           org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.submissionRepository = submissionRepository;
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
        this.topicProgressRepository = topicProgressRepository;
        this.recommendationRepository = recommendationRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            initAdmin();
        }
        initSampleStudents();
        initProblemsAndTestCases();
        if (submissionRepository.count() == 0 && studentRepository.count() > 0) {
            seedSampleStudentProgress();
        }
    }

    private void initAdmin() {
        Admin admin = new Admin("admin", "admin@dsalearning.com", "admin123");
        adminRepository.save(admin);
        logger.info("Initialized default Admin: admin@dsalearning.com / admin123");
    }

    private void initSampleStudents() {
        if (studentRepository.findByEmail("alex@college.edu").isEmpty()) {
            Student student = new Student("Alex Rivera", "alex@college.edu", "student123", "Stanford Institute of Tech", "Computer Science", 2025);
            studentRepository.save(student);
            logger.info("Initialized default Student: alex@college.edu / student123");
        }
        if (studentRepository.findByEmail("john@college.edu").isEmpty()) {
            Student student2 = new Student("john", "john@college.edu", "student123", "MIT", "Computer Science", 2025);
            studentRepository.save(student2);
            logger.info("Initialized default Student: john@college.edu / student123");
        }
    }

    private void initProblemsAndTestCases() {
        List<ProblemCatalog.ProblemDefinition> catalog = ProblemCatalog.get100Problems();
        boolean needsReseed = problemRepository.count() != catalog.size() || 
                              problemRepository.findAll().stream().anyMatch(p -> p.getAlternativeTopics() == null || p.getAlternativeTopics().trim().isEmpty());
        
        if (needsReseed) {
            logger.info("Seeding/Reseeding entire problem catalog with {} problems numbered cleanly from 1 to {}...", catalog.size(), catalog.size());
            recommendationRepository.deleteAll();
            testCaseRepository.deleteAll();
            submissionRepository.deleteAll();
            mistakeAnalysisRepository.deleteAll();
            topicProgressRepository.deleteAll();
            problemRepository.deleteAll();

            // Reset AUTO_INCREMENT / IDENTITY to 1 so problem IDs start cleanly from 1 to 100+
            try {
                jdbcTemplate.execute("ALTER TABLE problems ALTER COLUMN id RESTART WITH 1");
                jdbcTemplate.execute("ALTER TABLE test_cases ALTER COLUMN id RESTART WITH 1");
                jdbcTemplate.execute("ALTER TABLE submissions ALTER COLUMN id RESTART WITH 1");
                jdbcTemplate.execute("ALTER TABLE mistake_analysis ALTER COLUMN id RESTART WITH 1");
            } catch (Exception e1) {
                try {
                    jdbcTemplate.execute("ALTER TABLE problems AUTO_INCREMENT = 1");
                    jdbcTemplate.execute("ALTER TABLE test_cases AUTO_INCREMENT = 1");
                    jdbcTemplate.execute("ALTER TABLE submissions AUTO_INCREMENT = 1");
                    jdbcTemplate.execute("ALTER TABLE mistake_analysis AUTO_INCREMENT = 1");
                } catch (Exception e2) {
                    logger.debug("Table identity restart: {}", e2.getMessage());
                }
            }

            for (ProblemCatalog.ProblemDefinition def : catalog) {
                addProblem(
                    def.title,
                    def.description,
                    def.exampleInput,
                    def.exampleOutput,
                    def.constraints,
                    def.difficulty,
                    def.topic,
                    def.starterCode,
                    def.methodName,
                    def.returnType,
                    def.parameterTypes,
                    def.alternativeTopics,
                    def.supportedApproaches,
                    def.testCases
                );
            }
            logger.info("Successfully seeded " + problemRepository.count() + " DSA problems numbered cleanly from 1 to " + catalog.size() + "!");
            seedSampleStudentProgress();
        }
    }

    private void addProblem(String title, String description, String exampleInput, String exampleOutput,
                            String constraints, Difficulty difficulty, DsaTopic topic,
                            String starterCode, String methodName, String returnType, String paramTypes,
                            String altTopics, String supportedApproaches,
                            List<TestCase> testCases) {
        Problem p = new Problem();
        p.setTitle(title);
        p.setDescription(description);
        p.setExampleInput(exampleInput);
        p.setExampleOutput(exampleOutput);
        p.setConstraints(constraints);
        p.setDifficulty(difficulty);
        p.setTopic(topic);
        p.setStarterCode(starterCode);
        p.setMethodName(methodName);
        p.setReturnType(returnType);
        p.setParameterTypes(paramTypes);
        p.setAlternativeTopics(altTopics);
        p.setSupportedApproaches(supportedApproaches);
        p.setCreatedAt(LocalDateTime.now());

        Problem saved = problemRepository.save(p);
        for (TestCase tc : testCases) {
            tc.setProblemId(saved.getId());
            testCaseRepository.save(tc);
        }
    }

    private void seedSampleStudentProgress() {
        Student student = studentRepository.findByEmail("alex@college.edu").orElse(null);
        if (student == null) return;
        Long studentId = student.getId();

        logger.info("Seeding realistic submission history and mistake analytics for student alex@college.edu...");

        // Topic Progress Setup
        saveTopicProgress(studentId, DsaTopic.ARRAYS, 8, 7, 10, 85.0, 1.25);
        saveTopicProgress(studentId, DsaTopic.STRINGS, 7, 6, 9, 78.0, 1.28);
        saveTopicProgress(studentId, DsaTopic.HASHMAP, 6, 5, 8, 75.0, 1.33);
        saveTopicProgress(studentId, DsaTopic.STACK, 5, 4, 7, 71.0, 1.40);
        saveTopicProgress(studentId, DsaTopic.TWO_POINTER, 6, 4, 8, 62.5, 1.33);
        saveTopicProgress(studentId, DsaTopic.SLIDING_WINDOW, 5, 2, 7, 43.0, 2.50);
        saveTopicProgress(studentId, DsaTopic.RECURSION, 5, 1, 6, 33.3, 3.00);
        saveTopicProgress(studentId, DsaTopic.BINARY_SEARCH, 5, 4, 6, 66.7, 1.20);
        saveTopicProgress(studentId, DsaTopic.TREES, 4, 3, 5, 60.0, 1.25);
        saveTopicProgress(studentId, DsaTopic.DYNAMIC_PROGRAMMING, 4, 2, 6, 50.0, 2.00);

        // Submissions and Mistakes for Recursion (Problem 72: Fibonacci)
        List<Problem> recursionProblems = problemRepository.findByTopic(DsaTopic.RECURSION);
        if (!recursionProblems.isEmpty()) {
            Problem p = recursionProblems.get(0);
            Submission s1 = createSubmission(studentId, p.getId(), DsaTopic.RECURSION, SubmissionStatus.WRONG_ANSWER, 0, 3,
                    "class Solution {\n    public int solve(int n) {\n        return solve(n-1) + solve(n-2);\n    }\n}",
                    "StackOverflowError / Infinite recursion: Missing terminal base condition when n <= 1");
            createMistake(studentId, p.getId(), s1.getId(), DsaTopic.RECURSION, MistakeCategory.RECURSION_BASE_CASE,
                    "Infinite Recursion / Missing Base Case",
                    "The recursive function calls itself unconditionally without checking if n <= 1.",
                    "Missing termination guard check before recursive step.",
                    "Add 'if (n <= 1) return n;' at the start of your method.",
                    true, 3);
        }

        // Submissions and Mistakes for Sliding Window
        List<Problem> windowProblems = problemRepository.findByTopic(DsaTopic.SLIDING_WINDOW);
        if (!windowProblems.isEmpty()) {
            Problem p = windowProblems.get(0);
            Submission s2 = createSubmission(studentId, p.getId(), DsaTopic.SLIDING_WINDOW, SubmissionStatus.WRONG_ANSWER, 1, 2,
                    "class Solution {\n    public int solve(int[] nums, int k) {\n        int sum = 0;\n        for (int i=0; i<nums.length; i++) sum += nums[i];\n        return sum;\n    }\n}",
                    "Expected max subarray sum of size k, but calculated overall array sum");
            createMistake(studentId, p.getId(), s2.getId(), DsaTopic.SLIDING_WINDOW, MistakeCategory.POINTER_MOVEMENT,
                    "Incorrect Left Window Contraction",
                    "Window boundaries were expanded across the entire array without sliding or subtracting the left element.",
                    "Failed to subtract nums[left] when window size exceeds k.",
                    "Maintain left pointer and subtract elements when (right - left + 1) > k.",
                    true, 2);
        }

        logger.info("Finished seeding student sample data!");
    }

    private void saveTopicProgress(Long studentId, DsaTopic topic, int attempted, int solved, int subs, double acc, double avgAtt) {
        TopicProgress tp = new TopicProgress(studentId, topic);
        tp.setTotalAttempted(attempted);
        tp.setTotalSolved(solved);
        tp.setTotalSubmissions(subs);
        tp.setAccuracyPercentage(acc);
        tp.setAverageAttempts(avgAtt);
        tp.setLastPracticedAt(LocalDateTime.now().minusDays(1));
        topicProgressRepository.save(tp);
    }

    private Submission createSubmission(Long studentId, Long problemId, DsaTopic topic, SubmissionStatus status,
                                       int passed, int total, String code, String error) {
        Submission s = new Submission();
        s.setStudentId(studentId);
        s.setProblemId(problemId);
        s.setTopic(topic);
        s.setStatus(status);
        s.setTotalPassed(passed);
        s.setTotalTests(total);
        s.setSubmittedCode(code);
        s.setExecutionTimeMs(42L);
        s.setCompilerError(error);
        s.setCreatedAt(LocalDateTime.now().minusHours(2));
        return submissionRepository.save(s);
    }

    private void createMistake(Long studentId, Long problemId, Long submissionId, DsaTopic topic,
                               MistakeCategory cat, String specific, String explanation, String rootCause,
                               String fix, boolean recurring, int count) {
        MistakeAnalysis m = new MistakeAnalysis();
        m.setStudentId(studentId);
        m.setProblemId(problemId);
        m.setSubmissionId(submissionId);
        m.setTopic(topic);
        m.setMistakeCategory(cat);
        m.setSpecificMistake(specific);
        m.setExplanation(explanation);
        m.setRootCause(rootCause);
        m.setSuggestedFix(fix);
        m.setIsRecurring(recurring);
        m.setOccurrenceCount(count);
        m.setCreatedAt(LocalDateTime.now().minusHours(2));
        mistakeAnalysisRepository.save(m);
    }
}
