package com.odyssey.reviews;


import com.odyssey.activities.Activity;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.recommendations.Recommendation;
import com.odyssey.recommendations.RecommendationRegistrationRequest;
import com.odyssey.recommendations.RecommendationUpdateRequest;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewDao reviewDao;
    @Mock
    private UserDao userDao;
    @Mock
    private LocationDao locationDao;

    private ReviewService underTest;

    @BeforeEach
     void setUp() {
        underTest = new ReviewService(reviewDao,userDao,locationDao);

    }

    @Test
    void getAllReviews() {
        underTest.getAllReviews();
        verify(reviewDao).selectAllReviews();
    }

    @Test
    void getReview() {
        int id = 4;
        Review review = new Review(id,"The best place to visit", 5,new User(),new Location());
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.of(review));
        Review review1 = underTest.getReview(id);
        assertThat(review1).isEqualTo(review);

    }

    @Test
    void getReviewByUserId() {
        User user = new User();
        int user_id = 2;
        user.setId(user_id);

        when(userDao.existsUserById(user_id)).thenReturn(false);
        assertThatThrownBy(()-> underTest.getReviewByUserId(user_id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id [%s] not found".formatted(user_id));

        verify(reviewDao,never()).insertReview(any());
    }

    @Test
    void getReviewByLocationId() {
        Location location = new Location();
        int location_id = 2;
        location.setId(location_id);

        when(locationDao.existsLocationById(location_id)).thenReturn(false);
        assertThatThrownBy(()->underTest.getReviewByLocationId(location_id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Location with id [%s] not found".formatted(location_id));

        verify(reviewDao,never()).insertReview(any());
    }


    @Test
    void willThrowWhenGetReviewReturnsEmptyOptional() {
        int id = 3;
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getReview(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Review with id [%s] not found".formatted(id));


    }

    @Test
    void addReview() {
        User user = new User();
        Location location = new Location();

        int user_id = 3;
        int location_id = 3;

        user.setId(user_id);
        location.setId(location_id);

        String description = "The best place to visit";
        Integer rating = 5;

        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description,rating,user_id,location_id
        );

        when(userDao.selectUserById(user_id)).thenReturn(Optional.of(user));
        when(locationDao.selectLocationById(location_id)).thenReturn(Optional.of(location));

        when(reviewDao.existsReviewByUserAndLocationId(user_id,location_id)).thenReturn(false);

        underTest.addReview(reviewRegistrationRequest);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewDao).insertReview(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();

        Assertions.assertThat(capturedReview.getId()).isNull();
        Assertions.assertThat(capturedReview.getDescription()).isEqualTo(reviewRegistrationRequest.description());
        Assertions.assertThat(capturedReview.getRating()).isEqualTo(reviewRegistrationRequest.rating());
        Assertions.assertThat(capturedReview.getUser().getId()).isEqualTo(reviewRegistrationRequest.user_id());
        Assertions.assertThat(capturedReview.getLocation().getId()).isEqualTo(reviewRegistrationRequest.location_id());
    }


    @Test
    void willThrowReviewLocationNotExists() {
        User user = new User();
        int user_id = 1;
        user.setId(user_id);
        Location location =new Location();
        int location_id = 1;
        location.setId(location_id);

        String description = "Nice place, but the weather is bad";
        Integer rating = 3;
        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description,rating,user_id,location_id
        );

        when(locationDao.selectLocationById(location_id)).thenReturn(Optional.empty());
       lenient().when(reviewDao.existsReviewByLocationId(location_id)).thenReturn(false);
        assertThatThrownBy(()->underTest.addReview(reviewRegistrationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Location with id [%s] not found".formatted(location_id));

        verify(reviewDao,never()).insertReview(any());

    }

    @Test
    void willThrowAddReviewAlreadyExists() {
        User user = new User();
        Location location = new Location();
        int user_id = 2;
        int location_id = 2;
        user.setId(user_id);
        location.setId(location_id);

        String description = "The best place to visit";
        Integer rating = 5;

        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description,rating, user_id, location_id
        );
        lenient().when(userDao.selectUserById(user_id)).thenReturn(Optional.of(user));
        lenient().when(locationDao.selectLocationById(location_id)).thenReturn(Optional.of(location));

        when(reviewDao.existsReviewByUserAndLocationId(user_id,location_id)).thenReturn(true);

        Assertions.assertThatThrownBy(()->underTest.addReview(reviewRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Review already exists");

        verify(reviewDao, never()).insertReview(any());
    }

    @Test
    void deleteReview() {
        int id = 5;
        when(reviewDao.existsReviewById(id)).thenReturn(true);
        underTest.deleteReview(id);
        verify(reviewDao).deleteReviewById(id);
    }


    @Test
    void willThrowDeleteReviewNotExists() {
        int id = 5;
       lenient().when(reviewDao.existsReviewByUserId(id)).thenReturn(false);

        Assertions.assertThatThrownBy(()-> underTest.deleteReview(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Review with id [%s] not found".formatted(id));

        verify(reviewDao,never()).deleteReviewById(any());
    }


    @Test
    void updateReview() {
        int id = 3;
        User user = new User();
        user.setId(1);
        Location location = new Location();
        location.setId(1);
        String description = "The best";
        Integer rating = 5;

        Review review = new Review(description,rating, user,location);
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.of(review));

        String newdescription = "Good to visit";
        Integer newrating = 4;
        User user1 = new User();
        user1.setId(2);
        Location location1 = new Location();
        location1.setId(2);

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(newdescription, newrating,user1.getId(), location1.getId());
        when(userDao.selectUserById(user1.getId())).thenReturn(Optional.of(user1));
        when(locationDao.selectLocationById(location1.getId())).thenReturn(Optional.of(location1));
        underTest.updateReview(id,reviewUpdateRequest);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewDao).updateReview(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();

        Assertions.assertThat(capturedReview.getId()).isNull();
        Assertions.assertThat(capturedReview.getDescription()).isEqualTo(reviewUpdateRequest.description());
        Assertions.assertThat(capturedReview.getRating()).isEqualTo(reviewUpdateRequest.rating());
        Assertions.assertThat(capturedReview.getUser().getId()).isEqualTo(reviewUpdateRequest.user_id());
        Assertions.assertThat(capturedReview.getLocation().getId()).isEqualTo(reviewUpdateRequest.location_id());
    }


    @Test
    void willThrowUpdateReviewNoDataChanges() {
        int id = 3;
        User user = new User();
        user.setId(1);
        Location location = new Location();
        location.setId(1);
        String description = "Good place";
        Integer rating = 4;

        Review review = new Review(description,rating,user,location);
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.of(review));

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(
                review.getDescription(), review.getRating(),user.getId(),location.getId()
        );

        when(userDao.selectUserById(user.getId())).thenReturn(Optional.of(user));
        when(locationDao.selectLocationById(location.getId())).thenReturn(Optional.of(location));
        when(reviewDao.existsReviewByUserAndLocationId(reviewUpdateRequest.user_id(),reviewUpdateRequest.location_id())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> underTest.updateReview(id, reviewUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");


        verify(reviewDao, never()).updateReview(any());


    }
}
