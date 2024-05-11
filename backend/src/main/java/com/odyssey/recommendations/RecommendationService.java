package com.odyssey.recommendations;


import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService
{
    private final RecommendationDao recommendationDao ;
    private final UserDao userDao;
    private final ActivityDao activityDao;

    public RecommendationService(@Qualifier("recommendationJPAService")
                                 RecommendationDao recommendationDao,
                                 @Qualifier("userJPAService") UserDao userDao,
                                 @Qualifier("activityJPAService") ActivityDao activityDao) {
        this.recommendationDao = recommendationDao;
        this.userDao = userDao;
        this.activityDao = activityDao;
    }

    public List<Recommendation> getAllRecommendations(){
        return recommendationDao.selectAllRecommendations();
    }

    public Recommendation getRecommendationByActivityId(Integer activityId){
        if(!activityDao.existsActivityById(activityId)){
            throw new ResourceNotFoundException("activity with id [%s] not found".formatted(activityId));
        }
        return recommendationDao.selectRecommendationByActivityId(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("recommendation not found"));
    }
    public Recommendation getRecommendationByUserId(Integer userId){
        if(!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return recommendationDao.selectRecommendationByActivityId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("recommendation not found"));
    }

    public Recommendation getRecommendation(Integer id){
       return recommendationDao.selectRecommendationById(id)
               .orElseThrow(()-> new ResourceNotFoundException("recommendation with id [%s] not found ".formatted(id)));
    }

    public void addRecommendation(RecommendationRegistrationRequest recommendationRegistrationRequest){
        if(recommendationDao.existsRecommendationByUserIdAndActivityId
                (recommendationRegistrationRequest.userId(),recommendationRegistrationRequest.activityId())) {
            throw new DuplicateResourceException("recommendation already exists");
        }

        User user = userDao.selectUserById(recommendationRegistrationRequest.userId())
                .orElseThrow(()-> new ResourceNotFoundException("user with id [%s] not found"
                        .formatted(recommendationRegistrationRequest.userId())));

        Activity activity = activityDao.selectActivityById(recommendationRegistrationRequest.activityId())
                .orElseThrow(()-> new ResourceNotFoundException("activity with id [%s] not found"
                        .formatted(recommendationRegistrationRequest.activityId())));

        Recommendation recommendation = new Recommendation(
                recommendationRegistrationRequest.description(),
                user, activity
        );
        recommendationDao.insertRecommendation(recommendation);
    }

    public boolean deleteRecommendation(Integer id){
        if(recommendationDao.existsRecommendationById(id)){
            recommendationDao.deleteRecommendationById(id);
        }
        else{
            throw new ResourceNotFoundException("recommendation with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean updateRecommendation(Integer id, RecommendationUpdateRequest recommendationUpdateRequest){
        Recommendation existingRecommendation = getRecommendation(id);

        if(recommendationDao.existsRecommendationByUserIdAndActivityId(recommendationUpdateRequest.userId(), recommendationUpdateRequest.activityId())){
            throw new DuplicateResourceException("recommendation already exists");
        }

        User user = userDao.selectUserById(recommendationUpdateRequest.userId()).orElseThrow(
                () -> new ResourceNotFoundException("user with id [%s] not found".formatted(recommendationUpdateRequest.userId()))
        );

        Activity activity = activityDao.selectActivityById(recommendationUpdateRequest.activityId()).orElseThrow(
                () -> new ResourceNotFoundException("activity with id [%s] not found".formatted(recommendationUpdateRequest.activityId()))
        );

        boolean changes = false;

        if(recommendationUpdateRequest.description() != null && !recommendationUpdateRequest.description().equals(existingRecommendation.getDescription())){
            existingRecommendation.setDescription(recommendationUpdateRequest.description());
            changes = true;
        }

        if(recommendationUpdateRequest.userId() != null && !recommendationUpdateRequest.userId().equals(existingRecommendation.getUser().getId())){
            existingRecommendation.setUser(user);
            changes = true;
        }

        if(recommendationUpdateRequest.activityId() != null && !recommendationUpdateRequest.activityId().equals(existingRecommendation.getActivity().getId())){
            existingRecommendation.setActivity(activity);
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes were found");
        }

        recommendationDao.upateRecommendation(existingRecommendation);
        return changes;

    }




}
