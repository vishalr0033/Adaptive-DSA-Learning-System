package com.dsa.learning.dto;

import java.util.List;

public class KiwiChatRequestDto {

    private String message;
    private Long problemId;
    private String currentCode;
    private String lastError;
    private Long studentId;
    private List<ChatMessage> history;

    public KiwiChatRequestDto() {}

    public KiwiChatRequestDto(String message, Long problemId, String currentCode, String lastError) {
        this.message = message;
        this.problemId = problemId;
        this.currentCode = currentCode;
        this.lastError = lastError;
    }

    public static class ChatMessage {
        private String sender; // "user" or "kiwi"
        private String text;

        public ChatMessage() {}
        public ChatMessage(String sender, String text) {
            this.sender = sender;
            this.text = text;
        }

        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getCurrentCode() { return currentCode; }
    public void setCurrentCode(String currentCode) { this.currentCode = currentCode; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public List<ChatMessage> getHistory() { return history; }
    public void setHistory(List<ChatMessage> history) { this.history = history; }
}
