package com.dsa.learning.dto;

import com.dsa.learning.model.DsaTopic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisReportDto {
    private Long studentId;
    private String studentName;
    private double overallAccuracy;
    private double averageAttempts;
    private int totalMistakesLogged;

    // Identified Weakness Profile
    private DsaTopic primaryWeaknessTopic;
    private String primaryWeaknessName;
    private String primaryWeaknessMistake;
    private String primaryWeaknessExplanation;

    private DsaTopic secondaryWeaknessTopic;
    private String secondaryWeaknessName;
    private String secondaryWeaknessMistake;
    private String secondaryWeaknessExplanation;

    private List<String> strongAreas = new ArrayList<>();
    private List<String> weakAreas = new ArrayList<>();
    private String mostCommonMistake;
    private String recommendedNextTopic;

    private List<MistakeDetailDto> recurringMistakes = new ArrayList<>();
    private List<MistakeDetailDto> recentMistakes = new ArrayList<>();
    private Map<String, Long> mistakeCategoryDistribution = new HashMap<>();
    private List<TopicPerformanceDto> topicBreakdown = new ArrayList<>();
    private List<RecommendationDto> targetedPracticeQuestions = new ArrayList<>();

    public AnalysisReportDto() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public double getOverallAccuracy() { return overallAccuracy; }
    public void setOverallAccuracy(double overallAccuracy) { this.overallAccuracy = overallAccuracy; }

    public double getAverageAttempts() { return averageAttempts; }
    public void setAverageAttempts(double averageAttempts) { this.averageAttempts = averageAttempts; }

    public int getTotalMistakesLogged() { return totalMistakesLogged; }
    public void setTotalMistakesLogged(int totalMistakesLogged) { this.totalMistakesLogged = totalMistakesLogged; }

    public DsaTopic getPrimaryWeaknessTopic() { return primaryWeaknessTopic; }
    public void setPrimaryWeaknessTopic(DsaTopic primaryWeaknessTopic) {
        this.primaryWeaknessTopic = primaryWeaknessTopic;
        if (primaryWeaknessTopic != null) this.primaryWeaknessName = primaryWeaknessTopic.getDisplayName();
    }

    public String getPrimaryWeaknessName() { return primaryWeaknessName; }
    public void setPrimaryWeaknessName(String primaryWeaknessName) { this.primaryWeaknessName = primaryWeaknessName; }

    public String getPrimaryWeaknessMistake() { return primaryWeaknessMistake; }
    public void setPrimaryWeaknessMistake(String primaryWeaknessMistake) { this.primaryWeaknessMistake = primaryWeaknessMistake; }

    public String getPrimaryWeaknessExplanation() { return primaryWeaknessExplanation; }
    public void setPrimaryWeaknessExplanation(String primaryWeaknessExplanation) { this.primaryWeaknessExplanation = primaryWeaknessExplanation; }

    public DsaTopic getSecondaryWeaknessTopic() { return secondaryWeaknessTopic; }
    public void setSecondaryWeaknessTopic(DsaTopic secondaryWeaknessTopic) {
        this.secondaryWeaknessTopic = secondaryWeaknessTopic;
        if (secondaryWeaknessTopic != null) this.secondaryWeaknessName = secondaryWeaknessTopic.getDisplayName();
    }

    public String getSecondaryWeaknessName() { return secondaryWeaknessName; }
    public void setSecondaryWeaknessName(String secondaryWeaknessName) { this.secondaryWeaknessName = secondaryWeaknessName; }

    public String getSecondaryWeaknessMistake() { return secondaryWeaknessMistake; }
    public void setSecondaryWeaknessMistake(String secondaryWeaknessMistake) { this.secondaryWeaknessMistake = secondaryWeaknessMistake; }

    public String getSecondaryWeaknessExplanation() { return secondaryWeaknessExplanation; }
    public void setSecondaryWeaknessExplanation(String secondaryWeaknessExplanation) { this.secondaryWeaknessExplanation = secondaryWeaknessExplanation; }

    public List<String> getStrongAreas() { return strongAreas; }
    public void setStrongAreas(List<String> strongAreas) { this.strongAreas = strongAreas; }

    public List<String> getWeakAreas() { return weakAreas; }
    public void setWeakAreas(List<String> weakAreas) { this.weakAreas = weakAreas; }

    public String getMostCommonMistake() { return mostCommonMistake; }
    public void setMostCommonMistake(String mostCommonMistake) { this.mostCommonMistake = mostCommonMistake; }

    public String getRecommendedNextTopic() { return recommendedNextTopic; }
    public void setRecommendedNextTopic(String recommendedNextTopic) { this.recommendedNextTopic = recommendedNextTopic; }

    public List<MistakeDetailDto> getRecurringMistakes() { return recurringMistakes; }
    public void setRecurringMistakes(List<MistakeDetailDto> recurringMistakes) { this.recurringMistakes = recurringMistakes; }

    public List<MistakeDetailDto> getRecentMistakes() { return recentMistakes; }
    public void setRecentMistakes(List<MistakeDetailDto> recentMistakes) { this.recentMistakes = recentMistakes; }

    public Map<String, Long> getMistakeCategoryDistribution() { return mistakeCategoryDistribution; }
    public void setMistakeCategoryDistribution(Map<String, Long> mistakeCategoryDistribution) { this.mistakeCategoryDistribution = mistakeCategoryDistribution; }

    public List<TopicPerformanceDto> getTopicBreakdown() { return topicBreakdown; }
    public void setTopicBreakdown(List<TopicPerformanceDto> topicBreakdown) { this.topicBreakdown = topicBreakdown; }

    public List<RecommendationDto> getTargetedPracticeQuestions() { return targetedPracticeQuestions; }
    public void setTargetedPracticeQuestions(List<RecommendationDto> targetedPracticeQuestions) { this.targetedPracticeQuestions = targetedPracticeQuestions; }
}
