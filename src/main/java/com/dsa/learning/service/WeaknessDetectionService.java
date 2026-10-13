package com.dsa.learning.service;

import com.dsa.learning.dto.AnalysisReportDto;
import com.dsa.learning.dto.MistakeDetailDto;
import com.dsa.learning.dto.RecommendationDto;
import com.dsa.learning.dto.TopicPerformanceDto;
import com.dsa.learning.model.*;
import com.dsa.learning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeaknessDetectionService {

    private final TopicProgressRepository topicProgressRepository;
    private final MistakeAnalysisRepository mistakeAnalysisRepository;
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public WeaknessDetectionService(TopicProgressRepository topicProgressRepository,
                                    MistakeAnalysisRepository mistakeAnalysisRepository,
                                    SubmissionRepository submissionRepository,
                                    ProblemRepository problemRepository,
                                    StudentRepository studentRepository) {
        this.topicProgressRepository = topicProgressRepository;
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.studentRepository = studentRepository;
    }

    public AnalysisReportDto generateAnalysisReport(Long studentId) {
        AnalysisReportDto report = new AnalysisReportDto();
        report.setStudentId(studentId);

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            report.setStudentName(student.getName());
        }

        List<TopicProgress> progressList = topicProgressRepository.findByStudentId(studentId);
        List<MistakeAnalysis> allMistakes = mistakeAnalysisRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        List<Submission> allSubmissions = submissionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);

        report.setTotalMistakesLogged(allMistakes.size());

        // Global metrics
        long totalSubs = allSubmissions.size();
        long acceptedSubs = allSubmissions.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).count();
        double overallAcc = totalSubs > 0 ? ((double) acceptedSubs / totalSubs) * 100.0 : 0.0;
        report.setOverallAccuracy(Math.round(overallAcc * 10.0) / 10.0);

        long distinctAttempted = allSubmissions.stream().map(Submission::getProblemId).distinct().count();
        double avgAttempts = distinctAttempted > 0 ? ((double) totalSubs / distinctAttempted) : 0.0;
        report.setAverageAttempts(Math.round(avgAttempts * 10.0) / 10.0);

        // Topic performance breakdown
        List<TopicPerformanceDto> topicBreakdown = new ArrayList<>();
        Map<DsaTopic, List<MistakeAnalysis>> mistakesByTopic = allMistakes.stream()
                .collect(Collectors.groupingBy(MistakeAnalysis::getTopic));

        for (DsaTopic topic : DsaTopic.values()) {
            TopicProgress tp = progressList.stream()
                    .filter(p -> p.getTopic() == topic)
                    .findFirst()
                    .orElse(null);

            List<Submission> topicSubs = allSubmissions.stream()
                    .filter(s -> s.getTopic() == topic)
                    .toList();

            int attempted = tp != null ? tp.getTotalAttempted() : 0;
            int solved = tp != null ? tp.getTotalSolved() : 0;
            int subs = tp != null ? tp.getTotalSubmissions() : 0;
            double acc = tp != null ? tp.getAccuracyPercentage() : 0.0;
            double avgAtt = tp != null ? tp.getAverageAttempts() : 0.0;

            if (!topicSubs.isEmpty()) {
                long distinctAtt = topicSubs.stream().map(Submission::getProblemId).distinct().count();
                long distinctSol = topicSubs.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).map(Submission::getProblemId).distinct().count();
                long passCount = topicSubs.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).count();

                attempted = Math.max(attempted, (int) distinctAtt);
                solved = Math.max(solved, (int) distinctSol);
                subs = Math.max(subs, topicSubs.size());
                acc = (subs > 0) ? ((double) passCount / subs) * 100.0 : 0.0;
                avgAtt = (attempted > 0) ? ((double) subs / attempted) : 1.0;
            }

            TopicPerformanceDto dto = new TopicPerformanceDto(topic, attempted, solved, subs, Math.round(acc * 10.0) / 10.0, Math.round(avgAtt * 10.0) / 10.0);

            List<MistakeAnalysis> topicMistakes = mistakesByTopic.getOrDefault(topic, Collections.emptyList());
            dto.setMistakeCount(topicMistakes.size());

            if (!topicMistakes.isEmpty()) {
                // Find most frequent specific mistake in this topic
                Map<String, Long> freq = topicMistakes.stream()
                        .collect(Collectors.groupingBy(MistakeAnalysis::getSpecificMistake, Collectors.counting()));
                String topMistake = freq.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(topicMistakes.get(0).getSpecificMistake());
                dto.setPrimaryMistake(topMistake);
            }

            if (subs > 0) {
                topicBreakdown.add(dto);
            }
        }

        report.setTopicBreakdown(topicBreakdown);

        // Classify Strong & Weak Topics
        List<TopicPerformanceDto> sortedByAccuracy = new ArrayList<>(topicBreakdown);
        sortedByAccuracy.sort(Comparator.comparingDouble(TopicPerformanceDto::getAccuracyPercentage));

        List<String> weakAreas = new ArrayList<>();
        List<String> strongAreas = new ArrayList<>();

        for (TopicPerformanceDto tp : sortedByAccuracy) {
            if (tp.getAccuracyPercentage() < 60.0 && tp.getTotalSubmissions() > 0) {
                weakAreas.add(tp.getTopicName() + " (" + (int) tp.getAccuracyPercentage() + "%)");
            } else if (tp.getAccuracyPercentage() >= 75.0 && tp.getTotalSolved() > 0) {
                strongAreas.add(tp.getTopicName() + " (" + (int) tp.getAccuracyPercentage() + "%)");
            }
        }

        // Default fallbacks if brand new student
        if (strongAreas.isEmpty() && !topicBreakdown.isEmpty()) {
            strongAreas.add("Arrays");
            strongAreas.add("Strings");
        }
        if (weakAreas.isEmpty() && !topicBreakdown.isEmpty()) {
            weakAreas.add("Sliding Window");
            weakAreas.add("Recursion");
        }

        report.setStrongAreas(strongAreas);
        report.setWeakAreas(weakAreas);

        // Determine Primary Weakness and Secondary Weakness
        if (!sortedByAccuracy.isEmpty() && totalSubs > 0) {
            TopicPerformanceDto primaryWeak = sortedByAccuracy.get(0);
            report.setPrimaryWeaknessTopic(primaryWeak.getTopic());
            report.setPrimaryWeaknessMistake(primaryWeak.getPrimaryMistake() != null ?
                    primaryWeak.getPrimaryMistake() : "Boundary condition & logic errors");
            report.setPrimaryWeaknessExplanation(getTopicWeaknessExplanation(primaryWeak.getTopic(), primaryWeak.getPrimaryMistake()));

            if (sortedByAccuracy.size() > 1) {
                TopicPerformanceDto secondaryWeak = sortedByAccuracy.get(1);
                report.setSecondaryWeaknessTopic(secondaryWeak.getTopic());
                report.setSecondaryWeaknessMistake(secondaryWeak.getPrimaryMistake() != null ?
                        secondaryWeak.getPrimaryMistake() : "Algorithmic state transitions");
                report.setSecondaryWeaknessExplanation(getTopicWeaknessExplanation(secondaryWeak.getTopic(), secondaryWeak.getPrimaryMistake()));
            }
        } else {
            // New Student Onboarding Baseline
            report.setPrimaryWeaknessTopic(DsaTopic.ARRAYS);
            report.setPrimaryWeaknessMistake("Core Array Traversal & Indexing");
            report.setPrimaryWeaknessExplanation("Mastering single-pass linear scans and basic in-place array transformations.");
            report.setSecondaryWeaknessTopic(DsaTopic.STRINGS);
            report.setSecondaryWeaknessMistake("Two-Pointer String Traversal");
            report.setSecondaryWeaknessExplanation("Learning two-pointer inward scanning and character frequency counting.");
        }

        report.setRecommendedNextTopic(report.getPrimaryWeaknessTopic() != null ?
                report.getPrimaryWeaknessTopic().getDisplayName() : "Arrays");

        // Category distribution for charts
        Map<String, Long> catDist = new HashMap<>();
        for (MistakeAnalysis m : allMistakes) {
            String catName = m.getMistakeCategory().getDisplayName();
            catDist.put(catName, catDist.getOrDefault(catName, 0L) + 1);
        }
        report.setMistakeCategoryDistribution(catDist);

        // Most common mistake
        if (!allMistakes.isEmpty()) {
            Map<String, Long> mistakeCounts = allMistakes.stream()
                    .collect(Collectors.groupingBy(MistakeAnalysis::getSpecificMistake, Collectors.counting()));
            String mostCommon = mistakeCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Incorrect boundary condition");
            report.setMostCommonMistake(mostCommon);
        } else {
            report.setMostCommonMistake("Incorrect boundary condition");
        }

        // Map DTOs for recurring mistakes
        List<MistakeDetailDto> recurringDtos = allMistakes.stream()
                .filter(m -> Boolean.TRUE.equals(m.getIsRecurring()) || m.getOccurrenceCount() > 1)
                .map(this::toDetailDto)
                .collect(Collectors.toList());
        report.setRecurringMistakes(recurringDtos);

        List<MistakeDetailDto> recentDtos = allMistakes.stream()
                .limit(10)
                .map(this::toDetailDto)
                .collect(Collectors.toList());
        report.setRecentMistakes(recentDtos);

        return report;
    }

    private MistakeDetailDto toDetailDto(MistakeAnalysis m) {
        MistakeDetailDto dto = new MistakeDetailDto();
        dto.setId(m.getId());
        dto.setSubmissionId(m.getSubmissionId());
        dto.setProblemId(m.getProblemId());
        dto.setTopic(m.getTopic());
        dto.setMistakeCategory(m.getMistakeCategory());
        dto.setSpecificMistake(m.getSpecificMistake());
        dto.setExplanation(m.getExplanation());
        dto.setRootCause(m.getRootCause());
        dto.setSuggestedFix(m.getSuggestedFix());
        dto.setRecurring(Boolean.TRUE.equals(m.getIsRecurring()));
        dto.setOccurrenceCount(m.getOccurrenceCount());
        dto.setCreatedAt(m.getCreatedAt());

        problemRepository.findById(m.getProblemId()).ifPresent(p -> dto.setProblemTitle(p.getTitle()));
        return dto;
    }

    private String getTopicWeaknessExplanation(DsaTopic topic, String specificMistake) {
        if (topic == DsaTopic.SLIDING_WINDOW) {
            return "The student frequently fails to contract the window boundary when invalid conditions or duplicate characters are encountered.";
        } else if (topic == DsaTopic.RECURSION) {
            return "The student struggles with establishing proper base cases and handling returned subproblem states.";
        } else if (topic == DsaTopic.BINARY_SEARCH) {
            return "The student tends to produce off-by-one errors in search interval bounds or suffers from integer midpoint overflow.";
        } else if (topic == DsaTopic.DYNAMIC_PROGRAMMING) {
            return "The student has difficulty formulating recurrence relations and memoizing overlapping subproblems.";
        } else {
            return "The student struggles with boundary conditions and edge cases in " + topic.getDisplayName() + ".";
        }
    }
}
