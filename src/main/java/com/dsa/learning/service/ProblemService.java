package com.dsa.learning.service;

import com.dsa.learning.model.Difficulty;
import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.TestCase;
import com.dsa.learning.repository.ProblemRepository;
import com.dsa.learning.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository, TestCaseRepository testCaseRepository) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
    }

    public List<Problem> filterProblems(DsaTopic topic, Difficulty difficulty, String search) {
        if (search != null && search.trim().isEmpty()) search = null;
        return problemRepository.filterProblems(topic, difficulty, search);
    }

    public Optional<Problem> getProblemById(Long id) {
        return problemRepository.findById(id);
    }

    public List<TestCase> getTestCasesForProblem(Long problemId, boolean includeHidden) {
        if (includeHidden) {
            return testCaseRepository.findByProblemId(problemId);
        } else {
            return testCaseRepository.findByProblemIdAndIsSampleTrue(problemId);
        }
    }

    @Transactional
    public Problem saveProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    public DuplicateCheckResult checkDuplicate(String title, String starterCode, Long excludeId) {
        List<Problem> allProblems = problemRepository.findAll();

        // 1. Check problem title (case-insensitive, normalized spaces)
        if (title != null && !title.trim().isEmpty()) {
            String normTitle = title.trim().replaceAll("\\s+", " ").toLowerCase();
            for (Problem p : allProblems) {
                if (excludeId != null && excludeId.equals(p.getId())) {
                    continue;
                }
                if (p.getTitle() != null) {
                    String existingNormTitle = p.getTitle().trim().replaceAll("\\s+", " ").toLowerCase();
                    if (existingNormTitle.equals(normTitle)) {
                        return DuplicateCheckResult.duplicate(
                            "title",
                            "The code is already exists in Problem #" + p.getId() + " ('" + p.getTitle() + "')",
                            p
                        );
                    }
                }
            }
        }

        // 2. Check Java starter code (normalized, excluding generic default boilerplate)
        if (starterCode != null && !starterCode.trim().isEmpty()) {
            String normCode = normalizeCode(starterCode);
            if (normCode.length() > 25 && !isGenericBoilerplate(normCode)) {
                for (Problem p : allProblems) {
                    if (excludeId != null && excludeId.equals(p.getId())) {
                        continue;
                    }
                    if (p.getStarterCode() != null) {
                        String existingNormCode = normalizeCode(p.getStarterCode());
                        if (existingNormCode.length() > 25 && !isGenericBoilerplate(existingNormCode) && existingNormCode.equals(normCode)) {
                            return DuplicateCheckResult.duplicate(
                                "starterCode",
                                "The code is already exists in Problem #" + p.getId() + " ('" + p.getTitle() + "')",
                                p
                            );
                        }
                    }
                }
            }
        }

        return DuplicateCheckResult.noDuplicate();
    }

    private String normalizeCode(String code) {
        if (code == null) return "";
        // Strip single line comments
        String stripped = code.replaceAll("//.*", "");
        // Strip multi-line comments
        stripped = stripped.replaceAll("/\\*.*?\\*/", "");
        // Strip all whitespace
        return stripped.replaceAll("\\s+", "");
    }

    private boolean isGenericBoilerplate(String normCode) {
        return normCode.equals("classSolution{publicintsolve(int[]nums){}}")
            || normCode.equals("classSolution{publicvoidsolve(){}}")
            || normCode.equals("classSolution{publicintsolve(){}}");
    }

    @Transactional
    public void deleteProblem(Long id) {
        testCaseRepository.deleteByProblemId(id);
        problemRepository.deleteById(id);
    }

    @Transactional
    public TestCase saveTestCase(TestCase testCase) {
        return testCaseRepository.save(testCase);
    }

    public static class DuplicateCheckResult {
        private boolean duplicate;
        private String matchField;
        private String message;
        private Long existingId;
        private String existingTitle;
        private String existingTopic;
        private String existingDifficulty;

        public DuplicateCheckResult() {}

        public DuplicateCheckResult(boolean duplicate, String matchField, String message, Long existingId, String existingTitle, String existingTopic, String existingDifficulty) {
            this.duplicate = duplicate;
            this.matchField = matchField;
            this.message = message;
            this.existingId = existingId;
            this.existingTitle = existingTitle;
            this.existingTopic = existingTopic;
            this.existingDifficulty = existingDifficulty;
        }

        public static DuplicateCheckResult noDuplicate() {
            return new DuplicateCheckResult(false, null, "No duplicate detected", null, null, null, null);
        }

        public static DuplicateCheckResult duplicate(String matchField, String message, Problem existing) {
            return new DuplicateCheckResult(
                true,
                matchField,
                message,
                existing.getId(),
                existing.getTitle(),
                existing.getTopic() != null ? existing.getTopic().name() : null,
                existing.getDifficulty() != null ? existing.getDifficulty().name() : null
            );
        }

        public boolean isDuplicate() { return duplicate; }
        public void setDuplicate(boolean duplicate) { this.duplicate = duplicate; }

        public String getMatchField() { return matchField; }
        public void setMatchField(String matchField) { this.matchField = matchField; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Long getExistingId() { return existingId; }
        public void setExistingId(Long existingId) { this.existingId = existingId; }

        public String getExistingTitle() { return existingTitle; }
        public void setExistingTitle(String existingTitle) { this.existingTitle = existingTitle; }

        public String getExistingTopic() { return existingTopic; }
        public void setExistingTopic(String existingTopic) { this.existingTopic = existingTopic; }

        public String getExistingDifficulty() { return existingDifficulty; }
        public void setExistingDifficulty(String existingDifficulty) { this.existingDifficulty = existingDifficulty; }
    }
}
