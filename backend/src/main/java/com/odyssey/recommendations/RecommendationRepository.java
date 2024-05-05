package com.odyssey.recommendations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    boolean existsRecommendationById(Integer id);
    boolean existsRecommendationByUserIdAndActivityId(Integer userId,Integer activityId);
    boolean existsRecommendationByUserId(Integer userId);
    boolean existsRecommendationByActivityId(Integer activityId);
    Optional<Recommendation> findRecommendationByUserId(Integer userId);
    Optional<Recommendation>findRecommendationByActivityId(Integer activityId);
}
