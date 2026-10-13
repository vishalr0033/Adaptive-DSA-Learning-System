package com.dsa.learning.dto;

import com.dsa.learning.model.Difficulty;
import com.dsa.learning.model.DsaTopic;

public class RecommendationDto {
    private Long id;
    private Long problemId;
    private String problemTitle;
    private Difficulty difficulty;
    private DsaTopic topic;
    private String topicName;
    private String reason;
    private String targetedMistake;
    private int priority;
    private String status;

    public RecommendationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getProblemTitle() { return problemTitle; }
    public void setProblemTitle(String problemTitle) { this.problemTitle = problemTitle; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) {
        this.topic = topic;
        if (topic != null) this.topicName = topic.getDisplayName();
    }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getTargetedMistake() { return targetedMistake; }
    public void setTargetedMistake(String targetedMistake) { this.targetedMistake = targetedMistake; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
