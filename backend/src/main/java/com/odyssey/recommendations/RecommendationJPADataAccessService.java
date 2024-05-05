package com.odyssey.recommendations;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("recommendationJPAService")
public class RecommendationJPADataAccessService implements RecommendationDao {


    private final RecommendationRepository recommendationRepository;

    public RecommendationJPADataAccessService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public List<Recommendation> selectAllRecommendations() {
        return recommendationRepository.findAll();
    }

    @Override
    public Optional<Recommendation> selectRecommendationById(Integer id) {
        return recommendationRepository.findById(id);
    }

    @Override
    public Optional<Recommendation> selectRecommendationByUserId(Integer user_id) {
        return recommendationRepository.findRecommendationByUserId(user_id);
    }

    @Override
    public Optional<Recommendation> selectRecommendationByActivityId(Integer activity_id) {
        return recommendationRepository.findRecommendationByActivityId(activity_id);
    }

//    @Override
//    public Optional<Recommendation> selectRecommendationByUserAndActivityId(Integer user_id, Integer activity_id) {
//        return recommendationRepository.findRecommendationByUserAndActivityId(user_id,activity_id);
//    }

    @Override
    public void insertRecommendation(Recommendation recommendation) {
        recommendationRepository.save(recommendation);

    }

    @Override
    public void upateRecommendation(Recommendation recommendation) {
        recommendationRepository.save(recommendation);

    }

    @Override
    public boolean existsRecommendationById(Integer id) {
        return recommendationRepository.existsRecommendationById(id);
    }

    @Override
    public boolean existsRecommendationByUserIdAndActivityId(Integer user_id, Integer activity_id) {
        return recommendationRepository.existsRecommendationByUserIdAndActivityId(user_id,activity_id);
    }




    @Override
    public void deleteRecommendationById(Integer id) {
        recommendationRepository.deleteById(id);

    }
}
