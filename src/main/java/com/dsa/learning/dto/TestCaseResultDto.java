package com.dsa.learning.dto;

public class TestCaseResultDto {
    private int testIndex;
    private boolean passed;
    private boolean isSample;
    private boolean isHidden;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private String errorMessage;
    private Long executionTimeMs;

    public TestCaseResultDto() {}

    public int getTestIndex() { return testIndex; }
    public void setTestIndex(int testIndex) { this.testIndex = testIndex; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public boolean isSample() { return isSample; }
    public void setSample(boolean sample) { isSample = sample; }

    public boolean isHidden() { return isHidden; }
    public void setHidden(boolean hidden) { isHidden = hidden; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getActualOutput() { return actualOutput; }
    public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
}
