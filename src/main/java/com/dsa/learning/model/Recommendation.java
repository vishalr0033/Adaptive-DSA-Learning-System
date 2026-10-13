package com.dsa.learning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DsaTopic topic;

    @Column(nullable = false)
    private String reason;

    @Column(name = "targeted_mistake")
    private String targetedMistake;

    @Column(nullable = false)
    private Integer priority = 1;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, COMPLETED

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Recommendation() {}

    public Recommendation(Long studentId, Long problemId, DsaTopic topic, String reason, String targetedMistake, Integer priority) {
        this.studentId = studentId;
        this.problemId = problemId;
        this.topic = topic;
        this.reason = reason;
        this.targetedMistake = targetedMistake;
        this.priority = priority;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) { this.topic = topic; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getTargetedMistake() { return targetedMistake; }
    public void setTargetedMistake(String targetedMistake) { this.targetedMistake = targetedMistake; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
