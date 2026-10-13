package com.dsa.learning.controller;

import com.dsa.learning.model.MistakeAnalysis;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.Submission;
import com.dsa.learning.repository.MistakeAnalysisRepository;
import com.dsa.learning.repository.ProblemRepository;
import com.dsa.learning.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    private final SubmissionRepository submissionRepository;
    private final MistakeAnalysisRepository mistakeAnalysisRepository;
    private final ProblemRepository problemRepository;

    @Autowired
    public SubmissionController(SubmissionRepository submissionRepository,
                                MistakeAnalysisRepository mistakeAnalysisRepository,
                                ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
        this.problemRepository = problemRepository;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getStudentSubmissions(@PathVariable Long studentId) {
        List<Submission> submissions = submissionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Submission s : submissions) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("studentId", s.getStudentId());
            map.put("problemId", s.getProblemId());
            map.put("topic", s.getTopic() != null ? s.getTopic().getDisplayName() : "General");
            map.put("status", s.getStatus() != null ? s.getStatus().name() : "");
            map.put("executionTimeMs", s.getExecutionTimeMs());
            map.put("memoryUsed", s.getMemoryUsed());
            map.put("totalPassed", s.getTotalPassed());
            map.put("totalTests", s.getTotalTests());
            map.put("attemptNumber", s.getAttemptNumber());
            map.put("createdAt", s.getCreatedAt());

            problemRepository.findById(s.getProblemId()).ifPresent(p -> {
                map.put("problemTitle", p.getTitle());
                map.put("difficulty", p.getDifficulty().name());
            });

            Optional<MistakeAnalysis> analysis = mistakeAnalysisRepository.findBySubmissionId(s.getId());
            if (analysis.isPresent()) {
                map.put("hasMistakeAnalysis", true);
                map.put("mistakeCategory", analysis.get().getMistakeCategory().getDisplayName());
                map.put("specificMistake", analysis.get().getSpecificMistake());
            } else {
                map.put("hasMistakeAnalysis", false);
            }

            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSubmissionDetails(@PathVariable Long id) {
        Optional<Submission> subOpt = submissionRepository.findById(id);
        if (subOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Submission s = subOpt.get();
        Map<String, Object> map = new HashMap<>();
        map.put("id", s.getId());
        map.put("studentId", s.getStudentId());
        map.put("problemId", s.getProblemId());
        map.put("topic", s.getTopic() != null ? s.getTopic().getDisplayName() : "General");
        map.put("status", s.getStatus() != null ? s.getStatus().name() : "");
        map.put("submittedCode", s.getSubmittedCode());
        map.put("executionTimeMs", s.getExecutionTimeMs());
        map.put("memoryUsed", s.getMemoryUsed());
        map.put("totalPassed", s.getTotalPassed());
        map.put("totalTests", s.getTotalTests());
        map.put("attemptNumber", s.getAttemptNumber());
        map.put("failedTestCaseInput", s.getFailedTestCaseInput());
        map.put("actualOutput", s.getActualOutput());
        map.put("expectedOutput", s.getExpectedOutput());
        map.put("compilerError", s.getCompilerError());
        map.put("createdAt", s.getCreatedAt());

        problemRepository.findById(s.getProblemId()).ifPresent(p -> {
            map.put("problemTitle", p.getTitle());
            map.put("difficulty", p.getDifficulty().name());
        });

        Optional<MistakeAnalysis> analysis = mistakeAnalysisRepository.findBySubmissionId(s.getId());
        if (analysis.isPresent()) {
            MistakeAnalysis ma = analysis.get();
            map.put("hasMistakeAnalysis", true);
            map.put("mistakeCategory", ma.getMistakeCategory().getDisplayName());
            map.put("specificMistake", ma.getSpecificMistake());
            map.put("explanation", ma.getExplanation());
            map.put("rootCause", ma.getRootCause());
            map.put("suggestedFix", ma.getSuggestedFix());
            map.put("isRecurring", ma.getIsRecurring());
            map.put("occurrenceCount", ma.getOccurrenceCount());
        }

        return ResponseEntity.ok(map);
    }
}
