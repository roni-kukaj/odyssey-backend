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



    public Optional<Recommendation> getRecommendationByActivityId(Integer activity_id){
        if(!activityDao.existsActivityById(activity_id)){
            throw new ResourceNotFoundException("Activity with id [%s] not found".formatted(activity_id));
        }
        return recommendationDao.selectRecommendationByActivityId(activity_id);
    }
    public Optional<Recommendation> getRecommendationByUserId(Integer user_id){
        if(!userDao.existsUserById(user_id)){
            throw new ResourceNotFoundException("User with id [%s] not found".formatted(user_id));
        }
        return recommendationDao.selectRecommendationByUserId(user_id);
    }

    public Recommendation getRecommendation(Integer id){
       return recommendationDao.selectRecommendationById(id).orElseThrow(()-> new ResourceNotFoundException("Recommendation with id [%s] not found ".formatted(id)));


    }

    public void addRecommendation(RecommendationRegistrationRequest recommendationRegistrationRequest){
        if(recommendationDao.existsRecommendationByUserIdAndActivityId
                (recommendationRegistrationRequest.user_id(),recommendationRegistrationRequest.activity_id())) {
            throw new DuplicateResourceException("recommendation already exists");
        }

        User user = userDao.selectUserById(recommendationRegistrationRequest.user_id())
                .orElseThrow(()-> new ResourceNotFoundException("User with id [%s] not found"
                        .formatted(recommendationRegistrationRequest.user_id())));

        Activity activity = activityDao.selectActivityById(recommendationRegistrationRequest.activity_id())
                .orElseThrow(()-> new ResourceNotFoundException("Activity with id [%s] not found"
                        .formatted(recommendationRegistrationRequest.activity_id())));

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

        if(recommendationDao.existsRecommendationByUserIdAndActivityId(recommendationUpdateRequest.user_id(),recommendationUpdateRequest.activity_id())){
            throw new DuplicateResourceException("recommendation already exists");
        }

        User user = userDao.selectUserById(recommendationUpdateRequest.user_id()).orElseThrow(
                ()-> new ResourceNotFoundException("User with id [%s] not found".formatted(recommendationUpdateRequest.user_id()))
        );

        Activity activity = activityDao.selectActivityById(recommendationUpdateRequest.activity_id()).orElseThrow(
                ()->new ResourceNotFoundException("Activity with id [%s] not found".formatted(recommendationUpdateRequest.activity_id()))
        );

        boolean changes = false;

        if(recommendationUpdateRequest.description()!=null && !recommendationUpdateRequest.description().equals(existingRecommendation.getDescription())){
            existingRecommendation.setDescription(recommendationUpdateRequest.description());
            changes = true;
        }

        if(recommendationUpdateRequest.user_id()!=null && !recommendationUpdateRequest.user_id().equals(existingRecommendation.getUser().getId())){
            existingRecommendation.setUser(user);
            changes = true;
        }

        if(recommendationUpdateRequest.activity_id()!=null && !recommendationUpdateRequest.activity_id().equals(existingRecommendation.getActivity().getId())){
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
