package com.odyssey.recommendations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    boolean existsRecommendationById(Integer id);
    boolean existsRecommendationByUserIdAndActivityId(Integer user_id,Integer activity_id);

    boolean existsRecommendationByUserId(Integer user_id);
    boolean existsRecommendationByActivityId(Integer activity_id);



    Optional<Recommendation> findRecommendationByUserId(Integer user_id);
    Optional<Recommendation>findRecommendationByActivityId(Integer activity_id);

   // Optional<Recommendation> findRecommendationByUserAndActivityId(Integer userId, Integer activityId);

}
