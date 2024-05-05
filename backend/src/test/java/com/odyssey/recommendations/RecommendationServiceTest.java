package com.odyssey.recommendations;


import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.checkerframework.checker.units.qual.A;
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

    @BeforeEach
    void setUp(){
        underTest = new RecommendationService(recommendationDao,userDao,activityDao);
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

        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.of(recommendation));
        Recommendation recommendation1 = underTest.getRecommendation(id);
        assertThat(recommendation1).isEqualTo(recommendation);
    }




    @Test
    void getRecommendationByActivityId() {
        Activity activity = new Activity();
        Integer activityid = 2;
        activity.setId(activityid);
        when(activityDao.existsActivityById(activityid)).thenReturn(false);

        assertThatThrownBy(()->underTest.getRecommendationByActivityId(activityid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Activity with id [%s] not found".formatted(activityid));

        verify(recommendationDao,never()).insertRecommendation(any());
    }

    @Test
    void getRecommendationByUserId() {
        User user = new User();
        Integer userid = 2;
        user.setId(userid);

        when(userDao.existsUserById(userid)).thenReturn(false);

        assertThatThrownBy(()->underTest.getRecommendationByUserId(userid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id [%s] not found".formatted(userid));

        verify(recommendationDao,never()).insertRecommendation(any());
    }

    @Test
    void willThrowWhenGetRecommendationReturnEmptyOptional() {

        int id = 10;
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getRecommendation(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Recommendation with id [%s] not found ".formatted(id));
    }


    @Test
    void addRecommendation() {

        User user = new User();
        Activity activity = new Activity();

        int userid = 3;
        int activityid = 3;

        user.setId(userid);
        activity.setId(activityid);

        String description = "This place offers cultural experiences";

        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description,userid,activityid
        );

        when(userDao.selectUserById(userid)).thenReturn(Optional.of(user));
        when(activityDao.selectActivityById(activityid)).thenReturn(Optional.of(activity));

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userid,activityid)).thenReturn(false);

        underTest.addRecommendation(recommendationRegistrationRequest);

        ArgumentCaptor<Recommendation> recommendationArgumentCaptor = ArgumentCaptor.forClass(Recommendation.class);
        verify(recommendationDao).insertRecommendation(recommendationArgumentCaptor.capture());

        Recommendation capturedRecommendation = recommendationArgumentCaptor.getValue();

        assertThat(capturedRecommendation.getId()).isNull();
        assertThat(capturedRecommendation.getDescription()).isEqualTo(recommendationRegistrationRequest.description());
        assertThat(capturedRecommendation.getUser().getId()).isEqualTo(recommendationRegistrationRequest.user_id());
        assertThat(capturedRecommendation.getActivity().getId()).isEqualTo(recommendationRegistrationRequest.activity_id());

    }


    @Test
    void willThrowAddRecommendationUserNotExists() {
        User user = new User();
        Activity activity = new Activity();
        int userid = 3;
        int activityid = 3;
        user.setId(userid);
        activity.setId(activityid);
        String description = "A must - vist destination for any traveler seeking unforgettable memories";

        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description,userid,activityid
        );
        when(userDao.selectUserById(userid)).thenReturn(Optional.empty());

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userid,activityid)).thenReturn(false);

        assertThatThrownBy(()->underTest.addRecommendation(recommendationRegistrationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id [%s] not found".formatted(userid));

        verify(recommendationDao, never()).insertRecommendation(any());

    }



    @Test
    void willThrowAddRecommendationAlreadyExists() {
        User user = new User();
        Activity activity = new Activity();
        int userid = 2;
        int activityid = 2;
        user.setId(userid);
        activity.setId(activityid);

        String description = "A must - vist destination for any traveler seeking unforgettable memories";
        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description, userid,activityid
        );
        lenient().when(userDao.selectUserById(userid)).thenReturn(Optional.of(user));
        lenient().when(activityDao.selectActivityById(activityid)).thenReturn(Optional.of(activity));

        when(recommendationDao.existsRecommendationByUserIdAndActivityId(userid,activityid)).thenReturn(true);

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

        assertThatThrownBy(()-> underTest.deleteRecommendation(id))
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

        Recommendation recommendation = new Recommendation(description,user,activity);
        when(recommendationDao.selectRecommendationById(id)).thenReturn(Optional.of(recommendation));

        String newdescription = "Must visit the palace";
        User user1 = new User();
        user1.setId(2);
        Activity activity1 = new Activity();
        activity1.setId(2);

        RecommendationUpdateRequest recommendationUpdateRequest = new RecommendationUpdateRequest(newdescription, user1.getId(), activity1.getId());
        when(userDao.selectUserById(user1.getId())).thenReturn(Optional.of(user1));
        when(activityDao.selectActivityById(activity1.getId())).thenReturn(Optional.of(activity1));
        underTest.updateRecommendation(id,recommendationUpdateRequest);

        ArgumentCaptor<Recommendation> recommendationArgumentCaptor = ArgumentCaptor.forClass(Recommendation.class);
        verify(recommendationDao).upateRecommendation(recommendationArgumentCaptor.capture());

        Recommendation capturedRecommendation = recommendationArgumentCaptor.getValue();

        assertThat(capturedRecommendation.getId()).isNull();
        assertThat(capturedRecommendation.getDescription()).isEqualTo(recommendationUpdateRequest.description());
        assertThat(capturedRecommendation.getUser().getId()).isEqualTo(recommendationUpdateRequest.user_id());
        assertThat(capturedRecommendation.getActivity().getId()).isEqualTo(recommendationUpdateRequest.activity_id());
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

        RecommendationUpdateRequest recommendationUpdateRequest = new RecommendationUpdateRequest(
                recommendation.getDescription(), user.getId(),activity.getId()
        );

        when(userDao.selectUserById(user.getId())).thenReturn(Optional.of(user));
        when(activityDao.selectActivityById(activity.getId())).thenReturn(Optional.of(activity));
        when(recommendationDao.existsRecommendationByUserIdAndActivityId(recommendationUpdateRequest.user_id(),recommendationUpdateRequest.activity_id())).thenReturn(false);

        assertThatThrownBy(() -> underTest.updateRecommendation(id, recommendationUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");

        // Then
        verify(recommendationDao, never()).upateRecommendation(any());


    }
}
