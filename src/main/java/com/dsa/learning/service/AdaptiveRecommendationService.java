package com.dsa.learning.service;

import com.dsa.learning.dto.AnalysisReportDto;
import com.dsa.learning.dto.RecommendationDto;
import com.dsa.learning.model.*;
import com.dsa.learning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdaptiveRecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final WeaknessDetectionService weaknessDetectionService;
    private final MistakeAnalysisRepository mistakeAnalysisRepository;

    @Autowired
    public AdaptiveRecommendationService(RecommendationRepository recommendationRepository,
                                         ProblemRepository problemRepository,
                                         SubmissionRepository submissionRepository,
                                         WeaknessDetectionService weaknessDetectionService,
                                         MistakeAnalysisRepository mistakeAnalysisRepository) {
        this.recommendationRepository = recommendationRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.weaknessDetectionService = weaknessDetectionService;
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
    }

    @Transactional
    public List<RecommendationDto> generateAndGetRecommendations(Long studentId) {
        AnalysisReportDto analysis = weaknessDetectionService.generateAnalysisReport(studentId);
        List<Long> solvedProblemIds = submissionRepository.findSolvedProblemIdsByStudentId(studentId);
        List<MistakeAnalysis> recentMistakes = mistakeAnalysisRepository.findByStudentIdOrderByCreatedAtDesc(studentId);

        // Identify primary and secondary weak topics from student's actual performance
        DsaTopic primaryTopic = analysis.getPrimaryWeaknessTopic() != null ? analysis.getPrimaryWeaknessTopic() : DsaTopic.ARRAYS;
        DsaTopic secondaryTopic = analysis.getSecondaryWeaknessTopic() != null ? analysis.getSecondaryWeaknessTopic() : DsaTopic.STRINGS;

        String primaryMistake = (analysis.getPrimaryWeaknessMistake() != null && !analysis.getPrimaryWeaknessMistake().isEmpty())
                ? analysis.getPrimaryWeaknessMistake() : "Core Algorithm Logic & Edge Cases";
        String secondaryMistake = (analysis.getSecondaryWeaknessMistake() != null && !analysis.getSecondaryWeaknessMistake().isEmpty())
                ? analysis.getSecondaryWeaknessMistake() : "Pattern Recognition & Boundary Checks";

        // Fetch candidate problems from weak topics (both primary topic and alternative topics)
        List<Problem> primaryCandidates = new ArrayList<>(problemRepository.findByTopic(primaryTopic));
        List<Problem> secondaryCandidates = new ArrayList<>(problemRepository.findByTopic(secondaryTopic));

        // Also search alternative topics
        List<Problem> allProbs = problemRepository.findAll();
        for (Problem p : allProbs) {
            if (p.getAlternativeTopics() != null) {
                if (p.getAlternativeTopics().contains(primaryTopic.name()) && !primaryCandidates.contains(p)) {
                    primaryCandidates.add(p);
                }
                if (p.getAlternativeTopics().contains(secondaryTopic.name()) && !secondaryCandidates.contains(p)) {
                    secondaryCandidates.add(p);
                }
            }
        }

        // Sort problems progressively: EASY -> MEDIUM -> HARD
        Comparator<Problem> difficultyComparator = Comparator.comparing(Problem::getDifficulty);
        primaryCandidates.sort(difficultyComparator);
        secondaryCandidates.sort(difficultyComparator);

        recommendationRepository.deleteByStudentId(studentId);

        List<Recommendation> savedRecs = new ArrayList<>();
        int priorityCounter = 1;

        // Add progressive recommendations from primary weak topic
        for (Problem p : primaryCandidates) {
            if (!solvedProblemIds.contains(p.getId())) {
                Recommendation rec = new Recommendation();
                rec.setStudentId(studentId);
                rec.setProblemId(p.getId());
                rec.setTopic(p.getTopic());
                rec.setReason("Targeted practice for your diagnosed weakness in " + p.getTopic().getDisplayName() +
                        ". Builds mastery against: " + primaryMistake);
                rec.setTargetedMistake(primaryMistake);
                rec.setPriority(priorityCounter++);
                rec.setStatus("PENDING");
                rec.setCreatedAt(LocalDateTime.now());
                savedRecs.add(recommendationRepository.save(rec));
                if (priorityCounter > 4) break;
            }
        }

        // Add progressive recommendations from secondary weak topic
        for (Problem p : secondaryCandidates) {
            if (!solvedProblemIds.contains(p.getId()) && savedRecs.stream().noneMatch(r -> r.getProblemId().equals(p.getId()))) {
                Recommendation rec = new Recommendation();
                rec.setStudentId(studentId);
                rec.setProblemId(p.getId());
                rec.setTopic(p.getTopic());
                rec.setReason("Targeted practice for secondary area in " + p.getTopic().getDisplayName() +
                        ". Reinforces pattern against: " + secondaryMistake);
                rec.setTargetedMistake(secondaryMistake);
                rec.setPriority(priorityCounter++);
                rec.setStatus("PENDING");
                rec.setCreatedAt(LocalDateTime.now());
                savedRecs.add(recommendationRepository.save(rec));
                if (priorityCounter > 7) break;
            }
        }

        // If not enough recommendations, fill with foundational unsolved problems across catalog
        if (savedRecs.size() < 4) {
            allProbs.sort(difficultyComparator);
            for (Problem p : allProbs) {
                if (!solvedProblemIds.contains(p.getId()) && savedRecs.stream().noneMatch(r -> r.getProblemId().equals(p.getId()))) {
                    Recommendation rec = new Recommendation();
                    rec.setStudentId(studentId);
                    rec.setProblemId(p.getId());
                    rec.setTopic(p.getTopic());
                    rec.setReason("Foundational practice problem in " + p.getTopic().getDisplayName() + " to strengthen core algorithm patterns.");
                    rec.setTargetedMistake("Pattern Mastery");
                    rec.setPriority(priorityCounter++);
                    rec.setStatus("PENDING");
                    rec.setCreatedAt(LocalDateTime.now());
                    savedRecs.add(recommendationRepository.save(rec));
                    if (savedRecs.size() >= 6) break;
                }
            }
        }

        return getStudentRecommendations(studentId);
    }

    public List<RecommendationDto> getStudentRecommendations(Long studentId) {
        List<Recommendation> recs = recommendationRepository.findByStudentIdOrderByPriorityAscCreatedAtDesc(studentId);
        if (recs.isEmpty()) {
            return generateAndGetRecommendations(studentId);
        }

        List<RecommendationDto> dtos = new ArrayList<>();
        for (Recommendation r : recs) {
            Optional<Problem> probOpt = problemRepository.findById(r.getProblemId());
            if (probOpt.isEmpty()) {
                // Detected stale problem ID from earlier schema/data change - auto-regenerate fresh recommendations
                return generateAndGetRecommendations(studentId);
            }

            Problem p = probOpt.get();
            RecommendationDto dto = new RecommendationDto();
            dto.setId(r.getId());
            dto.setProblemId(r.getProblemId());
            dto.setProblemTitle(p.getTitle() != null ? p.getTitle() : ("Problem #" + p.getId()));
            dto.setDifficulty(p.getDifficulty() != null ? p.getDifficulty() : Difficulty.EASY);
            dto.setTopic(r.getTopic() != null ? r.getTopic() : p.getTopic());
            dto.setReason(r.getReason());
            dto.setTargetedMistake(r.getTargetedMistake() != null ? r.getTargetedMistake() : "Conceptual mastery");
            dto.setPriority(r.getPriority());
            dto.setStatus(r.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }
}
