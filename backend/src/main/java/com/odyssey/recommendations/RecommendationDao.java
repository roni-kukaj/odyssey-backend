package com.odyssey.recommendations;

import com.odyssey.events.Event;

import java.util.List;
import java.util.Optional;

public interface RecommendationDao {


    List<Recommendation> selectAllRecommendations();
    Optional<Recommendation> selectRecommendationById(Integer id);
    Optional<Recommendation> selectRecommendationByUserId(Integer user_id);
    Optional<Recommendation>selectRecommendationByActivityId(Integer activity_id);

    //Optional<Recommendation> selectRecommendationByUserAndActivityId(Integer user_id, Integer activity_id);
    void insertRecommendation(Recommendation recommendation);
    void upateRecommendation(Recommendation recommendation);
    boolean existsRecommendationById(Integer id);
    boolean existsRecommendationByUserIdAndActivityId(Integer user_id,Integer activity_id);






    void deleteRecommendationById(Integer id);
}
