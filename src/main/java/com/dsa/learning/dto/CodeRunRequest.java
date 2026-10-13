package com.dsa.learning.dto;

public class CodeRunRequest {
    private Long problemId;
    private String code;
    private String customInput;

    public CodeRunRequest() {}

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCustomInput() { return customInput; }
    public void setCustomInput(String customInput) { this.customInput = customInput; }
}
