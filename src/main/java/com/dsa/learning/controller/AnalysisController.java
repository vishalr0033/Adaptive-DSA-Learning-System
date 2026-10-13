package com.dsa.learning.controller;

import com.dsa.learning.dto.AnalysisReportDto;
import com.dsa.learning.service.WeaknessDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*")
public class AnalysisController {

    private final WeaknessDetectionService weaknessDetectionService;

    @Autowired
    public AnalysisController(WeaknessDetectionService weaknessDetectionService) {
        this.weaknessDetectionService = weaknessDetectionService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<AnalysisReportDto> getStudentAnalysis(@PathVariable Long studentId) {
        AnalysisReportDto report = weaknessDetectionService.generateAnalysisReport(studentId);
        return ResponseEntity.ok(report);
    }
}
