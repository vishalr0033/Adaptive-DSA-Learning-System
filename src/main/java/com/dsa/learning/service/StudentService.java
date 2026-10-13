package com.dsa.learning.service;

import com.dsa.learning.dto.*;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.Student;
import com.dsa.learning.model.Submission;
import com.dsa.learning.model.SubmissionStatus;
import com.dsa.learning.repository.ProblemRepository;
import com.dsa.learning.repository.StudentRepository;
import com.dsa.learning.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final WeaknessDetectionService weaknessDetectionService;
    private final AdaptiveRecommendationService recommendationService;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          SubmissionRepository submissionRepository,
                          ProblemRepository problemRepository,
                          WeaknessDetectionService weaknessDetectionService,
                          AdaptiveRecommendationService recommendationService) {
        this.studentRepository = studentRepository;
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.weaknessDetectionService = weaknessDetectionService;
        this.recommendationService = recommendationService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (studentRepository.existsByEmail(req.getEmail())) {
            return AuthResponse.failure("Email already registered with another account.");
        }

        Student student = new Student();
        student.setName(req.getName());
        student.setEmail(req.getEmail().trim().toLowerCase());
        student.setPassword(req.getPassword()); // Clean plain/salted for project demo
        student.setCollege(req.getCollege());
        student.setBranch(req.getBranch());
        student.setGraduationYear(req.getGraduationYear());

        Student saved = studentRepository.save(student);
        AuthResponse resp = AuthResponse.success(saved.getId(), saved.getName(), saved.getEmail(), "STUDENT");
        resp.setCollege(saved.getCollege());
        resp.setBranch(saved.getBranch());
        resp.setGraduationYear(saved.getGraduationYear());
        return resp;
    }

    public AuthResponse login(AuthRequest req) {
        Optional<Student> studentOpt = studentRepository.findByEmail(req.getEmail().trim().toLowerCase());
        if (studentOpt.isEmpty()) {
            return AuthResponse.failure("No student account found with this email address.");
        }

        Student student = studentOpt.get();
        if (!student.getPassword().equals(req.getPassword())) {
            return AuthResponse.failure("Incorrect password.");
        }

        AuthResponse resp = AuthResponse.success(student.getId(), student.getName(), student.getEmail(), "STUDENT");
        resp.setCollege(student.getCollege());
        resp.setBranch(student.getBranch());
        resp.setGraduationYear(student.getGraduationYear());
        return resp;
    }

    public StudentDashboardDto getDashboard(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);

        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setStudentId(studentId);
        dto.setStudentName(student != null ? student.getName() : "Student");
        dto.setStudentEmail(student != null ? student.getEmail() : "student@college.edu");

        List<Submission> allSubs = submissionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        long totalSubs = allSubs.size();
        long distinctAttempted = allSubs.stream().map(Submission::getProblemId).distinct().count();
        long distinctSolved = allSubs.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).map(Submission::getProblemId).distinct().count();
        long acceptedSubs = allSubs.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).count();

        dto.setTotalAttempted(distinctAttempted);
        dto.setTotalSolved(distinctSolved);
        dto.setTotalSubmissions(totalSubs);

        double acc = totalSubs > 0 ? ((double) acceptedSubs / totalSubs) * 100.0 : 0.0;
        dto.setOverallAccuracy(Math.round(acc * 10.0) / 10.0);
        dto.setImprovementPercentage(Math.min(100.0, Math.round((distinctSolved * 12.5) * 10.0) / 10.0));

        AnalysisReportDto analysis = weaknessDetectionService.generateAnalysisReport(studentId);
        dto.setTopicPerformances(analysis.getTopicBreakdown());

        // Strong vs Weak topic classification
        for (TopicPerformanceDto tp : analysis.getTopicBreakdown()) {
            if (tp.getAccuracyPercentage() >= 70.0 && tp.getTotalSolved() > 0) {
                dto.getStrongTopics().add(tp);
            } else if (tp.getAccuracyPercentage() < 60.0 && tp.getTotalSubmissions() > 0) {
                dto.getWeakTopics().add(tp);
            }
        }

        // Cache problem titles for fast rendering
        Map<Long, String> problemTitleMap = problemRepository.findAll().stream()
                .collect(Collectors.toMap(Problem::getId, Problem::getTitle, (a, b) -> a));

        List<Submission> recent = allSubs.stream().limit(8).peek(s -> {
            if (problemTitleMap.containsKey(s.getProblemId())) {
                s.setProblemTitle(problemTitleMap.get(s.getProblemId()));
            }
        }).toList();
        dto.setRecentSubmissions(recent);

        List<RecommendationDto> recs = recommendationService.getStudentRecommendations(studentId);
        dto.setRecommendations(recs);

        List<MistakeDetailDto> thoughtInsights = analysis.getRecurringMistakes() != null && !analysis.getRecurringMistakes().isEmpty() ?
                analysis.getRecurringMistakes() : analysis.getRecentMistakes();
        dto.setRecentThoughtDiagnoses(thoughtInsights != null ? thoughtInsights.stream().limit(4).toList() : List.of());

        return dto;
    }
}
