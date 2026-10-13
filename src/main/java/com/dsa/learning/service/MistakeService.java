package com.dsa.learning.service;

import com.dsa.learning.model.*;
import com.dsa.learning.repository.*;
import com.dsa.learning.service.ai.DeterministicDsaAnalyzer;
import com.dsa.learning.service.ai.GeminiAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MistakeService {

    private final MistakeAnalysisRepository mistakeAnalysisRepository;
    private final SubmissionRepository submissionRepository;
    private final TopicProgressRepository topicProgressRepository;
    private final GeminiAiService geminiAiService;

    @Autowired
    public MistakeService(MistakeAnalysisRepository mistakeAnalysisRepository,
                          SubmissionRepository submissionRepository,
                          TopicProgressRepository topicProgressRepository,
                          GeminiAiService geminiAiService) {
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
        this.submissionRepository = submissionRepository;
        this.topicProgressRepository = topicProgressRepository;
        this.geminiAiService = geminiAiService;
    }

    @Transactional
    public MistakeAnalysis processAndStoreMistake(Submission submission, Problem problem) {
        if (submission.getStatus() == SubmissionStatus.ACCEPTED) {
            updateTopicProgress(submission.getStudentId(), submission.getTopic(), true);
            return null;
        }

        // Update progress for failed submission
        updateTopicProgress(submission.getStudentId(), submission.getTopic(), false);

        // Run AI diagnosis
        DeterministicDsaAnalyzer.MistakeAnalysisResult diagnosis = geminiAiService.analyzeCodeSubmission(
                problem,
                submission.getSubmittedCode(),
                submission.getStatus(),
                submission.getCompilerError(),
                submission.getFailedTestCaseInput(),
                submission.getActualOutput(),
                submission.getExpectedOutput()
        );

        // Check history for recurring mistake patterns
        List<MistakeAnalysis> pastMistakes = mistakeAnalysisRepository.findByStudentIdAndTopicOrderByCreatedAtDesc(
                submission.getStudentId(), submission.getTopic()
        );

        int count = 1;
        boolean recurring = false;
        for (MistakeAnalysis past : pastMistakes) {
            if (past.getMistakeCategory() == diagnosis.category ||
                past.getSpecificMistake().equalsIgnoreCase(diagnosis.specificMistake) ||
                similarity(past.getSpecificMistake(), diagnosis.specificMistake) > 0.6) {
                count++;
                recurring = true;
            }
        }

        MistakeAnalysis analysis = new MistakeAnalysis();
        analysis.setSubmissionId(submission.getId());
        analysis.setStudentId(submission.getStudentId());
        analysis.setProblemId(problem.getId());
        analysis.setTopic(submission.getTopic());
        analysis.setMistakeCategory(diagnosis.category);
        analysis.setSpecificMistake(diagnosis.specificMistake);
        analysis.setExplanation(diagnosis.explanation);
        analysis.setRootCause(diagnosis.rootCause);
        analysis.setSuggestedFix(diagnosis.suggestedFix);
        analysis.setIsRecurring(recurring);
        analysis.setOccurrenceCount(count);
        analysis.setCreatedAt(LocalDateTime.now());

        return mistakeAnalysisRepository.save(analysis);
    }

    @Transactional
    public void updateTopicProgress(Long studentId, DsaTopic topic, boolean passed) {
        TopicProgress progress = topicProgressRepository.findByStudentIdAndTopic(studentId, topic)
                .orElseGet(() -> new TopicProgress(studentId, topic));

        progress.setTotalSubmissions(progress.getTotalSubmissions() + 1);
        if (passed) {
            progress.setTotalSolved(progress.getTotalSolved() + 1);
        }

        // Calculate unique problems attempted/solved for this student and topic
        List<Submission> topicSubmissions = submissionRepository.findByStudentIdAndTopicOrderByCreatedAtDesc(studentId, topic);
        long distinctAttempted = topicSubmissions.stream().map(Submission::getProblemId).distinct().count();
        long distinctSolved = topicSubmissions.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED)
                .map(Submission::getProblemId).distinct().count();

        progress.setTotalAttempted((int) distinctAttempted);
        progress.setTotalSolved((int) distinctSolved);

        double accuracy = (topicSubmissions.size() > 0) ?
                ((double) topicSubmissions.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).count() / topicSubmissions.size()) * 100.0 : 0.0;
        progress.setAccuracyPercentage(Math.round(accuracy * 10.0) / 10.0);

        double avgAttempts = (distinctAttempted > 0) ? ((double) topicSubmissions.size() / distinctAttempted) : 1.0;
        progress.setAverageAttempts(Math.round(avgAttempts * 10.0) / 10.0);
        progress.setLastPracticedAt(LocalDateTime.now());

        topicProgressRepository.save(progress);
    }

    private double similarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        String longer = s1.length() >= s2.length() ? s1.toLowerCase() : s2.toLowerCase();
        String shorter = s1.length() < s2.length() ? s1.toLowerCase() : s2.toLowerCase();
        if (longer.length() == 0) return 1.0;
        return (longer.length() - editDistance(longer, shorter)) / (double) longer.length();
    }

    private int editDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
