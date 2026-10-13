package com.dsa.learning.dto;

public class CodeSubmitRequest {
    private Long studentId;
    private Long problemId;
    private String code;

    public CodeSubmitRequest() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
