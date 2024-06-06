package com.odyssey.daos;

import com.odyssey.models.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationDao {
    List<Recommendation> selectAllRecommendations();
    Optional<Recommendation> selectRecommendationById(Integer id);
    Optional<Recommendation> selectRecommendationByUserId(Integer userId);
    Optional<Recommendation>selectRecommendationByActivityId(Integer activityId);
    void insertRecommendation(Recommendation recommendation);
    void upateRecommendation(Recommendation recommendation);
    boolean existsRecommendationById(Integer id);
    boolean existsRecommendationByUserIdAndActivityId(Integer userId,Integer activityId);
    void deleteRecommendationById(Integer id);
}
