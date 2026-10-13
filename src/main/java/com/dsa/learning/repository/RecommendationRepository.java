package com.dsa.learning.repository;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByStudentIdOrderByPriorityAscCreatedAtDesc(Long studentId);
    List<Recommendation> findByStudentIdAndStatusOrderByPriorityAsc(Long studentId, String status);
    List<Recommendation> findByStudentIdAndTopic(Long studentId, DsaTopic topic);
    void deleteByStudentId(Long studentId);
}
