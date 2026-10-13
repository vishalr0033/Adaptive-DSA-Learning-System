package com.dsa.learning.dto;

import com.dsa.learning.model.DsaTopic;

public class TopicPerformanceDto {
    private DsaTopic topic;
    private String topicName;
    private int totalAttempted;
    private int totalSolved;
    private int totalSubmissions;
    private double accuracyPercentage;
    private double averageAttempts;
    private boolean isWeak;
    private boolean isStrong;
    private String primaryMistake;
    private int mistakeCount;

    public TopicPerformanceDto() {}

    public TopicPerformanceDto(DsaTopic topic, int attempted, int solved, int submissions, double accuracy, double avgAttempts) {
        this.topic = topic;
        this.topicName = topic.getDisplayName();
        this.totalAttempted = attempted;
        this.totalSolved = solved;
        this.totalSubmissions = submissions;
        this.accuracyPercentage = accuracy;
        this.averageAttempts = avgAttempts;
        this.isWeak = accuracy < 55.0 && submissions > 0;
        this.isStrong = accuracy >= 75.0 && submissions > 0;
    }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) {
        this.topic = topic;
        if (topic != null) this.topicName = topic.getDisplayName();
    }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public int getTotalAttempted() { return totalAttempted; }
    public void setTotalAttempted(int totalAttempted) { this.totalAttempted = totalAttempted; }

    public int getTotalSolved() { return totalSolved; }
    public void setTotalSolved(int totalSolved) { this.totalSolved = totalSolved; }

    public int getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(int totalSubmissions) { this.totalSubmissions = totalSubmissions; }

    public double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }

    public double getAverageAttempts() { return averageAttempts; }
    public void setAverageAttempts(double averageAttempts) { this.averageAttempts = averageAttempts; }

    public boolean isWeak() { return isWeak; }
    public void setWeak(boolean weak) { isWeak = weak; }

    public boolean isStrong() { return isStrong; }
    public void setStrong(boolean strong) { isStrong = strong; }

    public String getPrimaryMistake() { return primaryMistake; }
    public void setPrimaryMistake(String primaryMistake) { this.primaryMistake = primaryMistake; }

    public int getMistakeCount() { return mistakeCount; }
    public void setMistakeCount(int mistakeCount) { this.mistakeCount = mistakeCount; }
}
