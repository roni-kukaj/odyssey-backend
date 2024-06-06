package com.odyssey.services;


import com.odyssey.daos.RecommendationDao;
import com.odyssey.dtos.RecommendationDto;
import com.odyssey.dtos.RecommendationRegistrationRequest;
import com.odyssey.models.Activity;
import com.odyssey.daos.ActivityDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Recommendation;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.RecommendationUpdateRequest;
import com.odyssey.services.utils.RecommendationDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService
{
    private final RecommendationDao recommendationDao ;
    private final UserDao userDao;
    private final ActivityDao activityDao;
    private final RecommendationDtoMapper recommendationDtoMapper;

    public RecommendationService(
            @Qualifier("recommendationJPAService") RecommendationDao recommendationDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("activityJPAService") ActivityDao activityDao,
            RecommendationDtoMapper recommendationDtoMapper
            ) {
        this.recommendationDao = recommendationDao;
        this.userDao = userDao;
        this.activityDao = activityDao;
        this.recommendationDtoMapper = recommendationDtoMapper;
    }

    private Recommendation getRecommendationById(Integer id) {
        return recommendationDao.selectRecommendationById(id)
                .orElseThrow(()-> new ResourceNotFoundException("recommendation with id [%s] not found ".formatted(id)));

    }

    public List<RecommendationDto> getAllRecommendations(){
        return recommendationDao.selectAllRecommendations()
                .stream().map(recommendationDtoMapper).collect(Collectors.toList());
    }

    public RecommendationDto getRecommendationByActivityId(Integer activityId){
        if(!activityDao.existsActivityById(activityId)){
            throw new ResourceNotFoundException("activity with id [%s] not found".formatted(activityId));
        }
        return recommendationDao.selectRecommendationByActivityId(activityId)
                .map(recommendationDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("recommendation not found"));
    }
    public RecommendationDto getRecommendationByUserId(Integer userId){
        if(!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return recommendationDao.selectRecommendationByActivityId(userId)
                .map(recommendationDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("recommendation not found"));
    }

    public RecommendationDto getRecommendation(Integer id){
       return recommendationDao.selectRecommendationById(id)
               .map(recommendationDtoMapper)
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

    public void deleteRecommendation(Integer id){
        if(recommendationDao.existsRecommendationById(id)){
            recommendationDao.deleteRecommendationById(id);
        }
        else {
            throw new ResourceNotFoundException("recommendation with id [%s] not found".formatted(id));
        }
    }

    public void updateRecommendation(Integer id, RecommendationUpdateRequest recommendationUpdateRequest){
        Recommendation existingRecommendation = getRecommendationById(id);

        boolean changes = false;

        if (recommendationUpdateRequest.description() != null && !recommendationUpdateRequest.description().equals(existingRecommendation.getDescription())){
            existingRecommendation.setDescription(recommendationUpdateRequest.description());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no changes were found");
        }

        recommendationDao.upateRecommendation(existingRecommendation);
    }
}