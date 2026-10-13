package com.dsa.learning.controller;

import com.dsa.learning.model.Difficulty;
import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.TestCase;
import com.dsa.learning.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "*")
public class ProblemController {

    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<List<Problem>> getProblems(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String search) {

        DsaTopic dsaTopic = null;
        if (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("ALL")) {
            dsaTopic = DsaTopic.fromString(topic);
        }

        Difficulty diff = null;
        if (difficulty != null && !difficulty.trim().isEmpty() && !difficulty.equalsIgnoreCase("ALL")) {
            try {
                diff = Difficulty.valueOf(difficulty.toUpperCase());
            } catch (Exception ignored) {}
        }

        List<Problem> problems = problemService.filterProblems(dsaTopic, diff, search);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/testcases")
    public ResponseEntity<List<TestCase>> getSampleTestCases(@PathVariable Long id) {
        List<TestCase> testCases = problemService.getTestCasesForProblem(id, false);
        return ResponseEntity.ok(testCases);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Map<String, String>>> getTopics() {
        List<Map<String, String>> topics = new ArrayList<>();
        for (DsaTopic t : DsaTopic.values()) {
            Map<String, String> m = new HashMap<>();
            m.put("key", t.name());
            m.put("name", t.getDisplayName());
            topics.add(m);
        }
        return ResponseEntity.ok(topics);
    }
}
