package com.dsa.learning.repository;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicProgressRepository extends JpaRepository<TopicProgress, Long> {
    List<TopicProgress> findByStudentIdOrderByAccuracyPercentageAsc(Long studentId);
    List<TopicProgress> findByStudentId(Long studentId);
    Optional<TopicProgress> findByStudentIdAndTopic(Long studentId, DsaTopic topic);
}
