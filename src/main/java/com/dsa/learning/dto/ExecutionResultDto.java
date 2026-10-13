package com.dsa.learning.dto;

import com.dsa.learning.model.SubmissionStatus;
import java.util.ArrayList;
import java.util.List;

public class ExecutionResultDto {
    private SubmissionStatus status;
    private String statusText;
    private Long submissionId;
    private Long executionTimeMs;
    private String memoryUsed;
    private String compilerError;
    private int passedCount;
    private int totalCount;
    private List<TestCaseResultDto> testCaseResults = new ArrayList<>();
    
    // AI Mistake Analysis info
    private boolean hasMistakeAnalysis;
    private String mistakeCategory;
    private String specificMistake;
    private String explanation;
    private String rootCause;
    private String suggestedFix;
    private boolean isRecurring;
    private int recurringCount;

    public ExecutionResultDto() {}

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) {
        this.status = status;
        this.statusText = status != null ? status.name() : null;
    }

    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public String getMemoryUsed() { return memoryUsed; }
    public void setMemoryUsed(String memoryUsed) { this.memoryUsed = memoryUsed; }

    public String getCompilerError() { return compilerError; }
    public void setCompilerError(String compilerError) { this.compilerError = compilerError; }

    public int getPassedCount() { return passedCount; }
    public void setPassedCount(int passedCount) { this.passedCount = passedCount; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public List<TestCaseResultDto> getTestCaseResults() { return testCaseResults; }
    public void setTestCaseResults(List<TestCaseResultDto> testCaseResults) { this.testCaseResults = testCaseResults; }

    public boolean isHasMistakeAnalysis() { return hasMistakeAnalysis; }
    public void setHasMistakeAnalysis(boolean hasMistakeAnalysis) { this.hasMistakeAnalysis = hasMistakeAnalysis; }

    public String getMistakeCategory() { return mistakeCategory; }
    public void setMistakeCategory(String mistakeCategory) { this.mistakeCategory = mistakeCategory; }

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

    public int getRecurringCount() { return recurringCount; }
    public void setRecurringCount(int recurringCount) { this.recurringCount = recurringCount; }
}
