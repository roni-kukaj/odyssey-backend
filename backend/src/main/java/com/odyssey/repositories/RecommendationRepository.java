package com.odyssey.repositories;

import com.odyssey.models.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    boolean existsRecommendationById(Integer id);
    boolean existsRecommendationByUserIdAndActivityId(Integer userId,Integer activityId);
    boolean existsRecommendationByUserId(Integer userId);
    boolean existsRecommendationByActivityId(Integer activityId);
    Optional<Recommendation> findRecommendationByUserId(Integer userId);
    Optional<Recommendation>findRecommendationByActivityId(Integer activityId);
}
