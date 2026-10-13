package com.dsa.learning.dto;

import com.dsa.learning.model.Submission;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboardDto {
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private long totalAttempted;
    private long totalSolved;
    private long totalSubmissions;
    private double overallAccuracy;
    private double improvementPercentage;
    
    private List<TopicPerformanceDto> topicPerformances = new ArrayList<>();
    private List<TopicPerformanceDto> strongTopics = new ArrayList<>();
    private List<TopicPerformanceDto> weakTopics = new ArrayList<>();
    private List<Submission> recentSubmissions = new ArrayList<>();
    private List<RecommendationDto> recommendations = new ArrayList<>();
    private List<MistakeDetailDto> recentThoughtDiagnoses = new ArrayList<>();

    public StudentDashboardDto() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public long getTotalAttempted() { return totalAttempted; }
    public void setTotalAttempted(long totalAttempted) { this.totalAttempted = totalAttempted; }

    public long getTotalSolved() { return totalSolved; }
    public void setTotalSolved(long totalSolved) { this.totalSolved = totalSolved; }

    public long getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(long totalSubmissions) { this.totalSubmissions = totalSubmissions; }

    public double getOverallAccuracy() { return overallAccuracy; }
    public void setOverallAccuracy(double overallAccuracy) { this.overallAccuracy = overallAccuracy; }

    public double getImprovementPercentage() { return improvementPercentage; }
    public void setImprovementPercentage(double improvementPercentage) { this.improvementPercentage = improvementPercentage; }

    public List<TopicPerformanceDto> getTopicPerformances() { return topicPerformances; }
    public void setTopicPerformances(List<TopicPerformanceDto> topicPerformances) { this.topicPerformances = topicPerformances; }

    public List<TopicPerformanceDto> getStrongTopics() { return strongTopics; }
    public void setStrongTopics(List<TopicPerformanceDto> strongTopics) { this.strongTopics = strongTopics; }

    public List<TopicPerformanceDto> getWeakTopics() { return weakTopics; }
    public void setWeakTopics(List<TopicPerformanceDto> weakTopics) { this.weakTopics = weakTopics; }

    public List<Submission> getRecentSubmissions() { return recentSubmissions; }
    public void setRecentSubmissions(List<Submission> recentSubmissions) { this.recentSubmissions = recentSubmissions; }

    public List<RecommendationDto> getRecommendations() { return recommendations; }
    public void setRecommendations(List<RecommendationDto> recommendations) { this.recommendations = recommendations; }

    public List<MistakeDetailDto> getRecentThoughtDiagnoses() { return recentThoughtDiagnoses; }
    public void setRecentThoughtDiagnoses(List<MistakeDetailDto> recentThoughtDiagnoses) { this.recentThoughtDiagnoses = recentThoughtDiagnoses; }
}
