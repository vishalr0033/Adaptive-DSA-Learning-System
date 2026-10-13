package com.dsa.learning.controller;

import com.dsa.learning.dto.AdminDashboardDto;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.TestCase;
import com.dsa.learning.service.AdminService;
import com.dsa.learning.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final ProblemService problemService;

    @Autowired
    public AdminController(AdminService adminService, ProblemService problemService) {
        this.adminService = adminService;
        this.problemService = problemService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboard() {
        AdminDashboardDto dto = adminService.getAdminDashboard();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/problems/check-duplicate")
    public ResponseEntity<ProblemService.DuplicateCheckResult> checkDuplicate(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String starterCode,
            @RequestParam(required = false) Long excludeId) {
        ProblemService.DuplicateCheckResult result = problemService.checkDuplicate(title, starterCode, excludeId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/problems")
    public ResponseEntity<?> createProblem(@RequestBody Problem problem) {
        ProblemService.DuplicateCheckResult dup = problemService.checkDuplicate(
            problem.getTitle(),
            problem.getStarterCode(),
            null
        );
        if (dup.isDuplicate()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "duplicate", true,
                "message", dup.getMessage(),
                "existingId", dup.getExistingId() != null ? dup.getExistingId() : 0,
                "existingTitle", dup.getExistingTitle() != null ? dup.getExistingTitle() : ""
            ));
        }
        Problem saved = problemService.saveProblem(problem);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/problems/{id}")
    public ResponseEntity<?> updateProblem(@PathVariable Long id, @RequestBody Problem problem) {
        ProblemService.DuplicateCheckResult dup = problemService.checkDuplicate(
            problem.getTitle(),
            problem.getStarterCode(),
            id
        );
        if (dup.isDuplicate()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "duplicate", true,
                "message", dup.getMessage(),
                "existingId", dup.getExistingId() != null ? dup.getExistingId() : 0,
                "existingTitle", dup.getExistingTitle() != null ? dup.getExistingTitle() : ""
            ));
        }
        problem.setId(id);
        Problem updated = problemService.saveProblem(problem);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/problems/{id}")
    public ResponseEntity<?> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.ok("{\"success\":true,\"message\":\"Problem deleted successfully\"}");
    }

    @PostMapping("/problems/{id}/testcases")
    public ResponseEntity<TestCase> addTestCase(@PathVariable Long id, @RequestBody TestCase testCase) {
        testCase.setProblemId(id);
        TestCase saved = problemService.saveTestCase(testCase);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/problems/{id}/testcases")
    public ResponseEntity<List<TestCase>> getProblemTestCases(@PathVariable Long id) {
        List<TestCase> cases = problemService.getTestCasesForProblem(id, true);
        return ResponseEntity.ok(cases);
    }
}
