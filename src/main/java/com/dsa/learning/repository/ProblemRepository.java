package com.dsa.learning.repository;

import com.dsa.learning.model.Difficulty;
import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByTitle(String title);
    List<Problem> findByTopic(DsaTopic topic);
    List<Problem> findByDifficulty(Difficulty difficulty);
    List<Problem> findByTopicAndDifficulty(DsaTopic topic, Difficulty difficulty);

    @Query("SELECT p FROM Problem p WHERE " +
           "(:topic IS NULL OR p.topic = :topic) AND " +
           "(:difficulty IS NULL OR p.difficulty = :difficulty) AND " +
           "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY p.id ASC")
    List<Problem> filterProblems(@Param("topic") DsaTopic topic,
                                 @Param("difficulty") Difficulty difficulty,
                                 @Param("search") String search);

    @Query("SELECT p.topic, COUNT(p) FROM Problem p GROUP BY p.topic")
    List<Object[]> countProblemsByTopic();
}
