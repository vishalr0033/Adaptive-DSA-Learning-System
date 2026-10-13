package com.dsa.learning.dto;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.MistakeCategory;
import java.time.LocalDateTime;

public class MistakeDetailDto {
    private Long id;
    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private DsaTopic topic;
    private String topicName;
    private MistakeCategory mistakeCategory;
    private String mistakeCategoryName;
    private String specificMistake;
    private String explanation;
    private String rootCause;
    private String suggestedFix;
    private boolean isRecurring;
    private int occurrenceCount;
    private LocalDateTime createdAt;

    public MistakeDetailDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getProblemTitle() { return problemTitle; }
    public void setProblemTitle(String problemTitle) { this.problemTitle = problemTitle; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) {
        this.topic = topic;
        if (topic != null) this.topicName = topic.getDisplayName();
    }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public MistakeCategory getMistakeCategory() { return mistakeCategory; }
    public void setMistakeCategory(MistakeCategory mistakeCategory) {
        this.mistakeCategory = mistakeCategory;
        if (mistakeCategory != null) this.mistakeCategoryName = mistakeCategory.getDisplayName();
    }

    public String getMistakeCategoryName() { return mistakeCategoryName; }
    public void setMistakeCategoryName(String mistakeCategoryName) { this.mistakeCategoryName = mistakeCategoryName; }

    public String getSpecificMistake() { return specificMistake; }
    public void setSpecificMistake(String specificMistake) { this.specificMistake = specificMistake; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getSuggestedFix() { return suggestedFix; }
    public void setSuggestedFix(String suggestedFix) { this.suggestedFix = suggestedFix; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public int getOccurrenceCount() { return occurrenceCount; }
    public void setOccurrenceCount(int occurrenceCount) { this.occurrenceCount = occurrenceCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
