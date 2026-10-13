package com.dsa.learning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

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

    @Column(name = "submitted_code", columnDefinition = "LONGTEXT", nullable = false)
    private String submittedCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs = 0L;

    @Column(name = "memory_used")
    private String memoryUsed;

    @Column(name = "failed_test_case_input", columnDefinition = "TEXT")
    private String failedTestCaseInput;

    @Column(name = "actual_output", columnDefinition = "TEXT")
    private String actualOutput;

    @Column(name = "expected_output", columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "compiler_error", columnDefinition = "TEXT")
    private String compilerError;

    @Column(name = "total_passed")
    private Integer totalPassed = 0;

    @Column(name = "total_tests")
    private Integer totalTests = 0;

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    private String problemTitle;

    public Submission() {}

    public String getProblemTitle() { return problemTitle; }
    public void setProblemTitle(String problemTitle) { this.problemTitle = problemTitle; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) { this.topic = topic; }

    public String getSubmittedCode() { return submittedCode; }
    public void setSubmittedCode(String submittedCode) { this.submittedCode = submittedCode; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public String getMemoryUsed() { return memoryUsed; }
    public void setMemoryUsed(String memoryUsed) { this.memoryUsed = memoryUsed; }

    public String getFailedTestCaseInput() { return failedTestCaseInput; }
    public void setFailedTestCaseInput(String failedTestCaseInput) { this.failedTestCaseInput = failedTestCaseInput; }

    public String getActualOutput() { return actualOutput; }
    public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }

    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getCompilerError() { return compilerError; }
    public void setCompilerError(String compilerError) { this.compilerError = compilerError; }

    public Integer getTotalPassed() { return totalPassed; }
    public void setTotalPassed(Integer totalPassed) { this.totalPassed = totalPassed; }

    public Integer getTotalTests() { return totalTests; }
    public void setTotalTests(Integer totalTests) { this.totalTests = totalTests; }

    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
