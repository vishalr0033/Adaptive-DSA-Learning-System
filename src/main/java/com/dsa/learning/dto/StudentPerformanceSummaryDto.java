package com.dsa.learning.dto;

import java.time.LocalDateTime;

public class StudentPerformanceSummaryDto {
    private Long id;
    private String name;
    private String email;
    private String college;
    private String branch;
    private Integer graduationYear;
    private LocalDateTime createdAt;

    private long totalSolved;
    private long totalAttempted;
    private long totalSubmissions;
    private double overallAccuracy;
    private String primaryWeaknessTopic;
    private String performanceStatus; // BEST, GOOD, AVERAGE, NEEDS_IMPROVEMENT, NOT_STARTED
    private String performanceLabel;  // 🌟 Best Performer, 👍 Good, etc.

    public StudentPerformanceSummaryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getTotalSolved() { return totalSolved; }
    public void setTotalSolved(long totalSolved) { this.totalSolved = totalSolved; }

    public long getTotalAttempted() { return totalAttempted; }
    public void setTotalAttempted(long totalAttempted) { this.totalAttempted = totalAttempted; }

    public long getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(long totalSubmissions) { this.totalSubmissions = totalSubmissions; }

    public double getOverallAccuracy() { return overallAccuracy; }
    public void setOverallAccuracy(double overallAccuracy) { this.overallAccuracy = overallAccuracy; }

    public String getPrimaryWeaknessTopic() { return primaryWeaknessTopic; }
    public void setPrimaryWeaknessTopic(String primaryWeaknessTopic) { this.primaryWeaknessTopic = primaryWeaknessTopic; }

    public String getPerformanceStatus() { return performanceStatus; }
    public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }

    public String getPerformanceLabel() { return performanceLabel; }
    public void setPerformanceLabel(String performanceLabel) { this.performanceLabel = performanceLabel; }
}
