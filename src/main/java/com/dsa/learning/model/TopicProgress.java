package com.dsa.learning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "topic"})
})
public class TopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DsaTopic topic;

    @Column(name = "total_attempted")
    private Integer totalAttempted = 0;

    @Column(name = "total_solved")
    private Integer totalSolved = 0;

    @Column(name = "total_submissions")
    private Integer totalSubmissions = 0;

    @Column(name = "accuracy_percentage")
    private Double accuracyPercentage = 0.0;

    @Column(name = "average_attempts")
    private Double averageAttempts = 0.0;

    @Column(name = "last_practiced_at")
    private LocalDateTime lastPracticedAt = LocalDateTime.now();

    public TopicProgress() {}

    public TopicProgress(Long studentId, DsaTopic topic) {
        this.studentId = studentId;
        this.topic = topic;
        this.totalAttempted = 0;
        this.totalSolved = 0;
        this.totalSubmissions = 0;
        this.accuracyPercentage = 0.0;
        this.averageAttempts = 0.0;
        this.lastPracticedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) { this.topic = topic; }

    public Integer getTotalAttempted() { return totalAttempted; }
    public void setTotalAttempted(Integer totalAttempted) { this.totalAttempted = totalAttempted; }

    public Integer getTotalSolved() { return totalSolved; }
    public void setTotalSolved(Integer totalSolved) { this.totalSolved = totalSolved; }

    public Integer getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(Integer totalSubmissions) { this.totalSubmissions = totalSubmissions; }

    public Double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(Double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }

    public Double getAverageAttempts() { return averageAttempts; }
    public void setAverageAttempts(Double averageAttempts) { this.averageAttempts = averageAttempts; }

    public LocalDateTime getLastPracticedAt() { return lastPracticedAt; }
    public void setLastPracticedAt(LocalDateTime lastPracticedAt) { this.lastPracticedAt = lastPracticedAt; }
}
