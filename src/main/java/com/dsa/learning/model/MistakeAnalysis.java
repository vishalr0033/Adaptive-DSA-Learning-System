package com.dsa.learning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mistake_analysis")
public class MistakeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DsaTopic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "mistake_category", nullable = false)
    private MistakeCategory mistakeCategory;

    @Column(name = "specific_mistake", nullable = false)
    private String specificMistake;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String explanation;

    @Column(name = "root_cause", columnDefinition = "TEXT")
    private String rootCause;

    @Column(name = "suggested_fix", columnDefinition = "TEXT")
    private String suggestedFix;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "occurrence_count")
    private Integer occurrenceCount = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public MistakeAnalysis() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) { this.topic = topic; }

    public MistakeCategory getMistakeCategory() { return mistakeCategory; }
    public void setMistakeCategory(MistakeCategory mistakeCategory) { this.mistakeCategory = mistakeCategory; }

    public String getSpecificMistake() { return specificMistake; }
    public void setSpecificMistake(String specificMistake) { this.specificMistake = specificMistake; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getSuggestedFix() { return suggestedFix; }
    public void setSuggestedFix(String suggestedFix) { this.suggestedFix = suggestedFix; }

    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }

    public Integer getOccurrenceCount() { return occurrenceCount; }
    public void setOccurrenceCount(Integer occurrenceCount) { this.occurrenceCount = occurrenceCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
