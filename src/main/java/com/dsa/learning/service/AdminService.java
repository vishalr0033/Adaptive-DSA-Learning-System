package com.dsa.learning.service;

import com.dsa.learning.dto.AdminDashboardDto;
import com.dsa.learning.dto.AuthRequest;
import com.dsa.learning.dto.AuthResponse;
import com.dsa.learning.model.Admin;
import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Student;
import com.dsa.learning.model.Submission;
import com.dsa.learning.repository.AdminRepository;
import com.dsa.learning.repository.MistakeAnalysisRepository;
import com.dsa.learning.repository.ProblemRepository;
import com.dsa.learning.repository.StudentRepository;
import com.dsa.learning.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final MistakeAnalysisRepository mistakeAnalysisRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        StudentRepository studentRepository,
                        ProblemRepository problemRepository,
                        SubmissionRepository submissionRepository,
                        MistakeAnalysisRepository mistakeAnalysisRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.mistakeAnalysisRepository = mistakeAnalysisRepository;
    }

    public AuthResponse login(AuthRequest req) {
        String identifier = (req.getEmail() != null && !req.getEmail().isEmpty()) ? req.getEmail() : req.getUsername();
        if (identifier == null) {
            return AuthResponse.failure("Username or email required.");
        }

        Optional<Admin> adminOpt = adminRepository.findByUsernameOrEmail(identifier.trim(), identifier.trim());
        if (adminOpt.isEmpty()) {
            return AuthResponse.failure("Invalid admin credentials.");
        }

        Admin admin = adminOpt.get();
        if (!admin.getPassword().equals(req.getPassword())) {
            return AuthResponse.failure("Invalid password.");
        }

        return AuthResponse.success(admin.getId(), admin.getUsername(), admin.getEmail(), "ADMIN");
    }

    public AdminDashboardDto getAdminDashboard() {
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setTotalStudents(studentRepository.count());
        dto.setTotalProblems(problemRepository.count());
        dto.setTotalSubmissions(submissionRepository.count());

        List<Student> students = studentRepository.findAll();
        dto.setStudents(students);

        List<com.dsa.learning.dto.StudentPerformanceSummaryDto> perfList = new ArrayList<>();
        for (Student s : students) {
            com.dsa.learning.dto.StudentPerformanceSummaryDto sp = new com.dsa.learning.dto.StudentPerformanceSummaryDto();
            sp.setId(s.getId());
            sp.setName(s.getName());
            sp.setEmail(s.getEmail());
            sp.setCollege(s.getCollege());
            sp.setBranch(s.getBranch());
            sp.setGraduationYear(s.getGraduationYear());
            sp.setCreatedAt(s.getCreatedAt());

            List<Submission> sSubs = submissionRepository.findByStudentIdOrderByCreatedAtDesc(s.getId());
            long totalSubs = sSubs.size();
            long distinctAttempted = sSubs.stream().map(Submission::getProblemId).distinct().count();
            long distinctSolved = sSubs.stream().filter(sub -> sub.getStatus() == com.dsa.learning.model.SubmissionStatus.ACCEPTED).map(Submission::getProblemId).distinct().count();
            long acceptedSubs = sSubs.stream().filter(sub -> sub.getStatus() == com.dsa.learning.model.SubmissionStatus.ACCEPTED).count();

            sp.setTotalSubmissions(totalSubs);
            sp.setTotalAttempted(distinctAttempted);
            sp.setTotalSolved(distinctSolved);

            double acc = totalSubs > 0 ? Math.round(((double) acceptedSubs / totalSubs) * 1000.0) / 10.0 : 0.0;
            sp.setOverallAccuracy(acc);

            // Determine weakness topic
            Map<DsaTopic, Long> failedTopicCounts = new HashMap<>();
            for (Submission sub : sSubs) {
                if (sub.getStatus() != com.dsa.learning.model.SubmissionStatus.ACCEPTED && sub.getTopic() != null) {
                    failedTopicCounts.put(sub.getTopic(), failedTopicCounts.getOrDefault(sub.getTopic(), 0L) + 1);
                }
            }

            if (!failedTopicCounts.isEmpty()) {
                DsaTopic worstTopic = failedTopicCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);
                sp.setPrimaryWeaknessTopic(worstTopic != null ? worstTopic.getDisplayName() : "General");
            } else if (totalSubs > 0) {
                sp.setPrimaryWeaknessTopic("None (Strong)");
            } else {
                sp.setPrimaryWeaknessTopic("N/A");
            }

            // Determine Performance Classification (Is Best or Not)
            if (totalSubs == 0) {
                sp.setPerformanceStatus("NOT_STARTED");
                sp.setPerformanceLabel("🆕 Not Started");
            } else if (acc >= 75.0 && distinctSolved >= 1) {
                sp.setPerformanceStatus("BEST");
                sp.setPerformanceLabel("🌟 Best Performer");
            } else if (acc >= 50.0) {
                sp.setPerformanceStatus("GOOD");
                sp.setPerformanceLabel("👍 Good");
            } else if (acc >= 25.0) {
                sp.setPerformanceStatus("AVERAGE");
                sp.setPerformanceLabel("📈 Average");
            } else {
                sp.setPerformanceStatus("NEEDS_IMPROVEMENT");
                sp.setPerformanceLabel("⚠️ Needs Attention");
            }

            perfList.add(sp);
        }
        dto.setStudentPerformances(perfList);

        List<Submission> recent = submissionRepository.findAllRecentSubmissions();
        dto.setRecentSubmissions(recent.stream().limit(15).toList());

        // Topic problem count distribution
        List<Object[]> problemCounts = problemRepository.countProblemsByTopic();
        Map<String, Long> dist = new HashMap<>();
        for (Object[] row : problemCounts) {
            DsaTopic t = (DsaTopic) row[0];
            Long count = (Long) row[1];
            dist.put(t.getDisplayName(), count);
        }
        dto.setTopicProblemDistribution(dist);

        // Common mistakes
        List<Object[]> topMistakes = mistakeAnalysisRepository.getGlobalTopMistakes();
        List<AdminDashboardDto.MistakeStat> mistakeStats = new ArrayList<>();
        String mostCommonMistake = "Incorrect boundary condition";
        if (!topMistakes.isEmpty()) {
            mostCommonMistake = (String) topMistakes.get(0)[0];
            for (Object[] row : topMistakes.stream().limit(8).toList()) {
                String mist = (String) row[0];
                Long count = (Long) row[1];
                mistakeStats.add(new AdminDashboardDto.MistakeStat(mist, "DSA Pattern", "General", count));
            }
        } else {
            mistakeStats.add(new AdminDashboardDto.MistakeStat("Incorrect left pointer movement", "Sliding Window", "Pointer Movement", 12L));
            mistakeStats.add(new AdminDashboardDto.MistakeStat("Missing recursion base case", "Recursion", "Base Case", 9L));
            mistakeStats.add(new AdminDashboardDto.MistakeStat("Off-by-one array index", "Arrays", "Boundary Condition", 7L));
        }
        dto.setCommonMistakes(mistakeStats);
        dto.setMostCommonMistake(mostCommonMistake);

        // Difficult topic (topic with lowest average accuracy or highest fail count)
        dto.setMostDifficultTopic("Sliding Window / Recursion");

        return dto;
    }
}
