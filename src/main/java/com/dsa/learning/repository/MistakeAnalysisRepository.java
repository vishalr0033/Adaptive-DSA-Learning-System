package com.dsa.learning.repository;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.MistakeAnalysis;
import com.dsa.learning.model.MistakeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MistakeAnalysisRepository extends JpaRepository<MistakeAnalysis, Long> {
    List<MistakeAnalysis> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<MistakeAnalysis> findByStudentIdAndTopicOrderByCreatedAtDesc(Long studentId, DsaTopic topic);
    Optional<MistakeAnalysis> findBySubmissionId(Long submissionId);

    @Query("SELECT m.mistakeCategory, COUNT(m) FROM MistakeAnalysis m WHERE m.studentId = :studentId GROUP BY m.mistakeCategory ORDER BY COUNT(m) DESC")
    List<Object[]> getMistakeCategoryCountsByStudent(@Param("studentId") Long studentId);

    @Query("SELECT m.specificMistake, m.topic, m.mistakeCategory, COUNT(m) FROM MistakeAnalysis m WHERE m.studentId = :studentId GROUP BY m.specificMistake, m.topic, m.mistakeCategory ORDER BY COUNT(m) DESC")
    List<Object[]> getTopSpecificMistakesByStudent(@Param("studentId") Long studentId);

    @Query("SELECT m.topic, m.mistakeCategory, COUNT(m) FROM MistakeAnalysis m GROUP BY m.topic, m.mistakeCategory ORDER BY COUNT(m) DESC")
    List<Object[]> getGlobalMistakePatterns();

    @Query("SELECT m.specificMistake, COUNT(m) FROM MistakeAnalysis m GROUP BY m.specificMistake ORDER BY COUNT(m) DESC")
    List<Object[]> getGlobalTopMistakes();
}
