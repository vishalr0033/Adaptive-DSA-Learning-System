package com.dsa.learning.controller;

import com.dsa.learning.dto.CodeRunRequest;
import com.dsa.learning.dto.CodeSubmitRequest;
import com.dsa.learning.dto.ExecutionResultDto;
import com.dsa.learning.execution.JavaCodeExecutor;
import com.dsa.learning.execution.TopicRestrictionValidator;
import com.dsa.learning.model.*;
import com.dsa.learning.repository.ProblemRepository;
import com.dsa.learning.repository.SubmissionRepository;
import com.dsa.learning.repository.TestCaseRepository;
import com.dsa.learning.service.AdaptiveRecommendationService;
import com.dsa.learning.service.MistakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "*")
public class ExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

    private final JavaCodeExecutor javaCodeExecutor;
    private final TopicRestrictionValidator topicRestrictionValidator;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final SubmissionRepository submissionRepository;
    private final MistakeService mistakeService;
    private final AdaptiveRecommendationService recommendationService;

    @Autowired
    public ExecutionController(JavaCodeExecutor javaCodeExecutor,
                               TopicRestrictionValidator topicRestrictionValidator,
                               ProblemRepository problemRepository,
                               TestCaseRepository testCaseRepository,
                               SubmissionRepository submissionRepository,
                               MistakeService mistakeService,
                               AdaptiveRecommendationService recommendationService) {
        this.javaCodeExecutor = javaCodeExecutor;
        this.topicRestrictionValidator = topicRestrictionValidator;
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.submissionRepository = submissionRepository;
        this.mistakeService = mistakeService;
        this.recommendationService = recommendationService;
    }

    @PostMapping("/run")
    public ResponseEntity<ExecutionResultDto> runCode(@RequestBody CodeRunRequest req) {
        Optional<Problem> problemOpt = problemRepository.findById(req.getProblemId());
        if (problemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Problem problem = problemOpt.get();

        List<TestCase> sampleCases = testCaseRepository.findByProblemIdAndIsSampleTrue(problem.getId());
        if (sampleCases.isEmpty()) {
            sampleCases = testCaseRepository.findByProblemId(problem.getId());
        }

        if (req.getCustomInput() != null && !req.getCustomInput().trim().isEmpty()) {
            TestCase customCase = new TestCase(problem.getId(), req.getCustomInput().trim(), "", true, false, "Custom Input");
            sampleCases = List.of(customCase);
        }

        // 1. Execute Java code on test cases first
        ExecutionResultDto result = javaCodeExecutor.execute(problem, req.getCode(), sampleCases);

        // 2. Perform Thought Process & Topic Paradigm Analysis if test cases passed
        if (result.getStatus() == SubmissionStatus.ACCEPTED) {
            TopicRestrictionValidator.ValidationResult approach = topicRestrictionValidator.validate(problem, req.getCode());
            if (!approach.isValid()) {
                result.setStatus(SubmissionStatus.SUBOPTIMAL_APPROACH);
                result.setHasMistakeAnalysis(true);
                result.setMistakeCategory(approach.getMistakeCategory().getDisplayName());
                result.setSpecificMistake(approach.getSpecificMistake());
                result.setExplanation(approach.getErrorMessage());
                result.setSuggestedFix(approach.getSuggestion());
            }
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/submit")
    public ResponseEntity<ExecutionResultDto> submitCode(@RequestBody CodeSubmitRequest req) {
        Optional<Problem> problemOpt = problemRepository.findById(req.getProblemId());
        if (problemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Problem problem = problemOpt.get();

        List<TestCase> allTestCases = testCaseRepository.findByProblemId(problem.getId());
        if (allTestCases.isEmpty()) {
            allTestCases = List.of(new TestCase(problem.getId(), problem.getExampleInput(), problem.getExampleOutput(), true, false, "Example"));
        }

        // 1. Execute Java code against all test cases
        ExecutionResultDto execResult = javaCodeExecutor.execute(problem, req.getCode(), allTestCases);

        // 2. Cognitive Thought-Process & Approach Verification
        TopicRestrictionValidator.ValidationResult approachResult = null;
        if (execResult.getStatus() == SubmissionStatus.ACCEPTED) {
            approachResult = topicRestrictionValidator.validate(problem, req.getCode());
            if (!approachResult.isValid()) {
                execResult.setStatus(SubmissionStatus.SUBOPTIMAL_APPROACH);
                execResult.setCompilerError(approachResult.getErrorMessage());
            }
        }

        // 3. Count prior attempts
        List<Submission> priorSubs = submissionRepository.findByStudentIdAndProblemIdOrderByCreatedAtDesc(
                req.getStudentId(), req.getProblemId()
        );
        int attemptNum = priorSubs.size() + 1;

        // 4. Save Submission in Database
        Submission submission = new Submission();
        submission.setStudentId(req.getStudentId());
        submission.setProblemId(req.getProblemId());
        submission.setTopic(problem.getTopic());
        submission.setSubmittedCode(req.getCode());
        submission.setStatus(execResult.getStatus());
        submission.setExecutionTimeMs(execResult.getExecutionTimeMs());
        submission.setMemoryUsed("24.8 MB");
        submission.setTotalPassed(execResult.getPassedCount());
        submission.setTotalTests(execResult.getTotalCount());
        submission.setAttemptNumber(attemptNum);
        submission.setCompilerError(execResult.getCompilerError());
        submission.setCreatedAt(LocalDateTime.now());

        if (execResult.getStatus() != SubmissionStatus.ACCEPTED && execResult.getStatus() != SubmissionStatus.SUBOPTIMAL_APPROACH) {
            execResult.getTestCaseResults().stream()
                    .filter(tc -> !tc.isPassed())
                    .findFirst()
                    .ifPresent(tc -> {
                        submission.setFailedTestCaseInput(tc.getInput());
                        submission.setActualOutput(tc.getActualOutput());
                        submission.setExpectedOutput(tc.getExpectedOutput());
                    });
        }

        Submission savedSub = submissionRepository.save(submission);
        execResult.setSubmissionId(savedSub.getId());

        // 5. AI Mistake / Thought Process Diagnosis
        if (execResult.getStatus() == SubmissionStatus.SUBOPTIMAL_APPROACH) {
            MistakeAnalysis analysis = mistakeService.processAndStoreMistake(savedSub, problem);
            execResult.setHasMistakeAnalysis(true);
            execResult.setMistakeCategory(approachResult.getMistakeCategory().getDisplayName());
            execResult.setSpecificMistake(approachResult.getSpecificMistake());
            execResult.setExplanation(approachResult.getErrorMessage());
            execResult.setSuggestedFix(approachResult.getSuggestion());
            if (analysis != null) {
                execResult.setRecurring(Boolean.TRUE.equals(analysis.getIsRecurring()));
                execResult.setRecurringCount(analysis.getOccurrenceCount());
            }
            mistakeService.updateTopicProgress(savedSub.getStudentId(), savedSub.getTopic(), false);
        } else if (execResult.getStatus() != SubmissionStatus.ACCEPTED) {
            try {
                MistakeAnalysis analysis = mistakeService.processAndStoreMistake(savedSub, problem);
                if (analysis != null) {
                    execResult.setHasMistakeAnalysis(true);
                    execResult.setMistakeCategory(analysis.getMistakeCategory().getDisplayName());
                    execResult.setSpecificMistake(analysis.getSpecificMistake());
                    execResult.setExplanation(analysis.getExplanation());
                    execResult.setRootCause(analysis.getRootCause());
                    execResult.setSuggestedFix(analysis.getSuggestedFix());
                    execResult.setRecurring(Boolean.TRUE.equals(analysis.getIsRecurring()));
                    execResult.setRecurringCount(analysis.getOccurrenceCount());
                }
            } catch (Exception e) {
                logger.error("Failed to run AI Mistake Analysis", e);
            }
            mistakeService.updateTopicProgress(savedSub.getStudentId(), savedSub.getTopic(), false);
        } else {
            mistakeService.updateTopicProgress(savedSub.getStudentId(), savedSub.getTopic(), true);
        }

        // 6. Dynamically refresh student's adaptive practice path based on new activity
        try {
            recommendationService.generateAndGetRecommendations(savedSub.getStudentId());
        } catch (Exception e) {
            logger.warn("Failed to auto-refresh recommendations: {}", e.getMessage());
        }

        return ResponseEntity.ok(execResult);
    }
}
