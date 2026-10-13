package com.dsa.learning.dto;

import com.dsa.learning.model.DsaTopic;
import java.util.ArrayList;
import java.util.List;

public class ImprovementReportDto {
    private Long studentId;
    private String studentName;
    private double overallImprovementRate;

    private List<TopicImprovementCard> topicImprovements = new ArrayList<>();

    public static class TopicImprovementCard {
        private DsaTopic topic;
        private String topicName;
        private double preAccuracy;
        private double postAccuracy;
        private double preAvgAttempts;
        private double postAvgAttempts;
        private int preMistakeCount;
        private int postMistakeCount;
        private String recurringMistakeSummary;
        private double accuracyGain;

        public TopicImprovementCard() {}

        public DsaTopic getTopic() { return topic; }
        public void setTopic(DsaTopic topic) {
            this.topic = topic;
            if (topic != null) this.topicName = topic.getDisplayName();
        }

        public String getTopicName() { return topicName; }
        public void setTopicName(String topicName) { this.topicName = topicName; }

        public double getPreAccuracy() { return preAccuracy; }
        public void setPreAccuracy(double preAccuracy) { this.preAccuracy = preAccuracy; }

        public double getPostAccuracy() { return postAccuracy; }
        public void setPostAccuracy(double postAccuracy) { this.postAccuracy = postAccuracy; }

        public double getPreAvgAttempts() { return preAvgAttempts; }
        public void setPreAvgAttempts(double preAvgAttempts) { this.preAvgAttempts = preAvgAttempts; }

        public double getPostAvgAttempts() { return postAvgAttempts; }
        public void setPostAvgAttempts(double postAvgAttempts) { this.postAvgAttempts = postAvgAttempts; }

        public int getPreMistakeCount() { return preMistakeCount; }
        public void setPreMistakeCount(int preMistakeCount) { this.preMistakeCount = preMistakeCount; }

        public int getPostMistakeCount() { return postMistakeCount; }
        public void setPostMistakeCount(int postMistakeCount) { this.postMistakeCount = postMistakeCount; }

        public String getRecurringMistakeSummary() { return recurringMistakeSummary; }
        public void setRecurringMistakeSummary(String recurringMistakeSummary) { this.recurringMistakeSummary = recurringMistakeSummary; }

        public double getAccuracyGain() { return accuracyGain; }
        public void setAccuracyGain(double accuracyGain) { this.accuracyGain = accuracyGain; }
    }

    public ImprovementReportDto() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public double getOverallImprovementRate() { return overallImprovementRate; }
    public void setOverallImprovementRate(double overallImprovementRate) { this.overallImprovementRate = overallImprovementRate; }

    public List<TopicImprovementCard> getTopicImprovements() { return topicImprovements; }
    public void setTopicImprovements(List<TopicImprovementCard> topicImprovements) { this.topicImprovements = topicImprovements; }
}
