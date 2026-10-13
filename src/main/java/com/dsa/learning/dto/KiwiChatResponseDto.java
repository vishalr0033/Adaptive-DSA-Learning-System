package com.dsa.learning.dto;

import java.util.List;

public class KiwiChatResponseDto {

    private String reply;
    private List<String> suggestedQuestions;
    private String kiwiMood; // "friendly", "hinting", "celebrating", "analyzing"
    private boolean success;

    public KiwiChatResponseDto() {}

    public KiwiChatResponseDto(String reply, List<String> suggestedQuestions, String kiwiMood, boolean success) {
        this.reply = reply;
        this.suggestedQuestions = suggestedQuestions;
        this.kiwiMood = kiwiMood;
        this.success = success;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public List<String> getSuggestedQuestions() { return suggestedQuestions; }
    public void setSuggestedQuestions(List<String> suggestedQuestions) { this.suggestedQuestions = suggestedQuestions; }

    public String getKiwiMood() { return kiwiMood; }
    public void setKiwiMood(String kiwiMood) { this.kiwiMood = kiwiMood; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
