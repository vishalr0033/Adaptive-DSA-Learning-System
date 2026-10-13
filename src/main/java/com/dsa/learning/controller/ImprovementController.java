package com.dsa.learning.controller;

import com.dsa.learning.dto.ImprovementReportDto;
import com.dsa.learning.service.ImprovementTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/improvement")
@CrossOrigin(origins = "*")
public class ImprovementController {

    private final ImprovementTrackingService improvementTrackingService;

    @Autowired
    public ImprovementController(ImprovementTrackingService improvementTrackingService) {
        this.improvementTrackingService = improvementTrackingService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ImprovementReportDto> getImprovementReport(@PathVariable Long studentId) {
        ImprovementReportDto report = improvementTrackingService.calculateImprovementReport(studentId);
        return ResponseEntity.ok(report);
    }
}
