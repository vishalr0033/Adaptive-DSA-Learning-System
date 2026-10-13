package com.dsa.learning.controller;

import com.dsa.learning.dto.RecommendationDto;
import com.dsa.learning.service.AdaptiveRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final AdaptiveRecommendationService recommendationService;

    @Autowired
    public RecommendationController(AdaptiveRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(@PathVariable Long studentId) {
        List<RecommendationDto> recs = recommendationService.getStudentRecommendations(studentId);
        return ResponseEntity.ok(recs);
    }

    @PostMapping("/generate/{studentId}")
    public ResponseEntity<List<RecommendationDto>> regenerateRecommendations(@PathVariable Long studentId) {
        List<RecommendationDto> recs = recommendationService.generateAndGetRecommendations(studentId);
        return ResponseEntity.ok(recs);
    }
}
