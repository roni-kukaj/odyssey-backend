package com.odyssey.recommendations;


import com.odyssey.daos.RecommendationDao;
import com.odyssey.dtos.RecommendationDto;
import com.odyssey.dtos.RecommendationRegistrationRequest;
import com.odyssey.dtos.RecommendationUpdateRequest;
import com.odyssey.models.Activity;
import com.odyssey.daos.ActivityDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Recommendation;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.services.RecommendationService;
import com.odyssey.services.utils.RecommendationDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationDao recommendationDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ActivityDao activityDao;

    private RecommendationService underTest;

    private final RecommendationDtoMapper recommendationDtoMapper = new RecommendationDtoMapper();

    @BeforeEach
    void setUp(){
        underTest = new RecommendationService(recommendationDao,userDao,activityDao, recommendationDtoMapper);
    }

    @Test
    void getAllRecommendations() {
        underTest.getAllRecommendations();
        verify(recommendationDao).selectAllRecommendations();
    }

    @Test
    void getRecommendation() {
        int id = 10;
        Recommendation recommendation = new Recommendation(
                id,
                "A must - vist destination for any traveler seeking unforgettable memories",
                new User(),
                new Activity()
        );
        RecommendationDto recommendationDto = recommendationDtoMapper.apply(recommendation);
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.of(recommendation));
        RecommendationDto recommendation1 = underTest.getRecommendation(id);
        assertThat(recommendation1).isEqualTo(recommendationDto);
    }

    @Test
    void getRecommendationByActivityId() {
        Activity activity = new Activity();
        Integer activityId = 2;
        activity.setId(activityId);
        when(activityDao.existsActivityById(activityId)).thenReturn(false);

        assertThatThrownBy( () -> underTest.getRecommendationByActivityId(activityId) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("activity with id [%s] not found".formatted(activityId));

        verify(recommendationDao,never()).insertRecommendation(any());
    }

    @Test
    void getRecommendationByUserId() {
        User user = new User();
        Integer userId = 2;
        user.setId(userId);

        when(userDao.existsUserById(userId)).thenReturn(false);

        assertThatThrownBy( ()->underTest.getRecommendationByUserId(userId) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(userId));

        verify(recommendationDao,never()).insertRecommendation(any());
    }

    @Test
    void willThrowWhenGetRecommendationReturnEmptyOptional() {

        int id = 10;
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> underTest.getRecommendation(id) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("recommendation with id [%s] not found ".formatted(id));
    }


    @Test
    void addRecommendation() {

        User user = new User();
        Activity activity = new Activity();

        int userId = 3;
        int activityId = 3;

        user.setId(userId);
        activity.setId(activityId);

        String description = "This place offers cultural experiences";

        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description, userId, activityId
        );

        when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(activityDao.selectActivityById(activityId)).thenReturn(Optional.of(activity));

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userId, activityId)).thenReturn(false);

        underTest.addRecommendation(recommendationRegistrationRequest);

        ArgumentCaptor<Recommendation> recommendationArgumentCaptor = ArgumentCaptor.forClass(Recommendation.class);
        verify(recommendationDao).insertRecommendation(recommendationArgumentCaptor.capture());

        Recommendation capturedRecommendation = recommendationArgumentCaptor.getValue();

        assertThat(capturedRecommendation.getId()).isNull();
        assertThat(capturedRecommendation.getDescription()).isEqualTo(recommendationRegistrationRequest.description());
        assertThat(capturedRecommendation.getUser().getId()).isEqualTo(recommendationRegistrationRequest.userId());
        assertThat(capturedRecommendation.getActivity().getId()).isEqualTo(recommendationRegistrationRequest.activityId());

    }


    @Test
    void willThrowAddRecommendationUserNotExists() {
        User user = new User();
        Activity activity = new Activity();
        int userId = 3;
        int activityId = 3;
        user.setId(userId);
        activity.setId(activityId);
        String description = "A must - vist destination for any traveler seeking unforgettable memories";

        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description, userId, activityId
        );
        when(userDao.selectUserById(userId)).thenReturn(Optional.empty());

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userId,activityId)).thenReturn(false);

        assertThatThrownBy( () -> underTest.addRecommendation(recommendationRegistrationRequest) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(userId));

        verify(recommendationDao, never()).insertRecommendation(any());

    }



    @Test
    void willThrowAddRecommendationAlreadyExists() {
        User user = new User();
        Activity activity = new Activity();
        int userId = 2;
        int activityId = 2;
        user.setId(userId);
        activity.setId(activityId);

        String description = "A must - vist destination for any traveler seeking unforgettable memories";
        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description, userId, activityId
        );
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        lenient().when(activityDao.selectActivityById(activityId)).thenReturn(Optional.of(activity));

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userId,activityId)).thenReturn(true);

        assertThatThrownBy(()->underTest.addRecommendation(recommendationRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("recommendation already exists");

        verify(recommendationDao, never()).insertRecommendation(any());
    }



    @Test
    void deleteRecommendation() {
        int id = 2;
        when(recommendationDao.existsRecommendationById(id)).thenReturn(true);
        underTest.deleteRecommendation(id);
        verify(recommendationDao).deleteRecommendationById(id);
    }

    @Test
    void willThrowDeleteRecommendationNotExists() {
        int id = 3;
        when(recommendationDao.existsRecommendationById(id)).thenReturn(false);

        assertThatThrownBy( () -> underTest.deleteRecommendation(id) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("recommendation with id [%s] not found".formatted(id));

        verify(recommendationDao,never()).deleteRecommendationById(any());
    }

    @Test
    void updateRecommendation() {
        int id = 3;
        User user = new User();
        user.setId(1);
        Activity activity = new Activity();
        activity.setId(1);
        String description = "Must visit";

        Recommendation recommendation = new Recommendation(description, user, activity);
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.of(recommendation));

        String newDescription = "Must visit the palace";

        RecommendationUpdateRequest request =
                new RecommendationUpdateRequest(
                        newDescription
                );

        underTest.updateRecommendation(id, request);

        ArgumentCaptor<Recommendation> recommendationArgumentCaptor = ArgumentCaptor.forClass(Recommendation.class);
        verify(recommendationDao).upateRecommendation(recommendationArgumentCaptor.capture());

        Recommendation capturedRecommendation = recommendationArgumentCaptor.getValue();

        assertThat(capturedRecommendation.getId()).isNull();
        assertThat(capturedRecommendation.getDescription()).isEqualTo(request.description());
        assertThat(capturedRecommendation.getUser().getId()).isEqualTo(user.getId());
        assertThat(capturedRecommendation.getActivity().getId()).isEqualTo(activity.getId());
    }

    @Test
    void willThrowUpdateRecommendationNoDataChanges() {
        int id = 3;
        User user = new User();
        user.setId(1);
        Activity activity = new Activity();
        activity.setId(1);
        String description = "Must visit";

        Recommendation recommendation = new Recommendation(description,user,activity);
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.of(recommendation));

        RecommendationUpdateRequest request = new RecommendationUpdateRequest(
                recommendation.getDescription()
        );

        assertThatThrownBy(() -> underTest.updateRecommendation(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");

        // Then
        verify(recommendationDao, never()).upateRecommendation(any());


    }
}
