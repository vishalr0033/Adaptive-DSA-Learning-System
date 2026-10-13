package com.dsa.learning.repository;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Submission;
import com.dsa.learning.model.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Submission> findByStudentIdAndProblemIdOrderByCreatedAtDesc(Long studentId, Long problemId);
    List<Submission> findByStudentIdAndTopicOrderByCreatedAtDesc(Long studentId, DsaTopic topic);

    long countByStudentId(Long studentId);
    long countByStudentIdAndStatus(Long studentId, SubmissionStatus status);

    @Query("SELECT COUNT(DISTINCT s.problemId) FROM Submission s WHERE s.studentId = :studentId")
    long countDistinctAttemptedProblems(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(DISTINCT s.problemId) FROM Submission s WHERE s.studentId = :studentId AND s.status = com.dsa.learning.model.SubmissionStatus.ACCEPTED")
    long countDistinctSolvedProblems(@Param("studentId") Long studentId);

    @Query("SELECT DISTINCT s.problemId FROM Submission s WHERE s.studentId = :studentId AND s.status = com.dsa.learning.model.SubmissionStatus.ACCEPTED")
    List<Long> findSolvedProblemIdsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT s.topic, COUNT(s), SUM(CASE WHEN s.status = com.dsa.learning.model.SubmissionStatus.ACCEPTED THEN 1 ELSE 0 END) " +
           "FROM Submission s WHERE s.studentId = :studentId GROUP BY s.topic")
    List<Object[]> getTopicSubmissionStats(@Param("studentId") Long studentId);

    @Query("SELECT s FROM Submission s ORDER BY s.createdAt DESC")
    List<Submission> findAllRecentSubmissions();
}
