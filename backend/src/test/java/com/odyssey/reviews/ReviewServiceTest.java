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

    private final ReviewDtoMapper reviewDtoMapper = new ReviewDtoMapper();

    private ReviewService underTest;

    @BeforeEach
     void setUp() {
        underTest = new ReviewService(reviewDao, userDao, locationDao, reviewDtoMapper);
    }

    @Test
    void getAllReviews() {
        underTest.getAllReviews();
        verify(reviewDao).selectAllReviews();
    }

    @Test
    void getReview() {
        int id = 4;
        Review review = new Review(id,"The best place to visit", 5, new User(), new Location());
        ReviewDto reviewDto = reviewDtoMapper.apply(review);
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.of(review));
        ReviewDto review1 = underTest.getReview(id);
        assertThat(review1).isEqualTo(reviewDto);
    }

    @Test
    void getReviewByUserId() {
        User user = new User();
        int userId = 2;
        user.setId(userId);

        when(userDao.existsUserById(userId)).thenReturn(false);
        assertThatThrownBy( () -> underTest.getReviewByUserId(userId) )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(userId));

        verify(reviewDao,never()).insertReview(any());
    }

    @Test
    void getReviewByLocationId() {
        Location location = new Location();
        int locationId = 2;
        location.setId(locationId);

        when(locationDao.existsLocationById(locationId)).thenReturn(false);
        assertThatThrownBy(() -> underTest.getReviewByLocationId(locationId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        verify(reviewDao,never()).insertReview(any());
    }


    @Test
    void willThrowWhenGetReviewReturnsEmptyOptional() {
        int id = 3;
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getReview(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("review with id [%s] not found".formatted(id));


    }

    @Test
    void addReview() {
        User user = new User();
        Location location = new Location();

        int userId = 3;
        int locationId = 3;

        user.setId(userId);
        location.setId(locationId);

        String description = "The best place to visit";
        Integer rating = 5;

        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description, rating, userId, locationId
        );

        when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));

        when(reviewDao.existsReviewByUserAndLocationId(userId, locationId)).thenReturn(false);

        underTest.addReview(reviewRegistrationRequest);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewDao).insertReview(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();

        Assertions.assertThat(capturedReview.getId()).isNull();
        Assertions.assertThat(capturedReview.getDescription()).isEqualTo(reviewRegistrationRequest.description());
        Assertions.assertThat(capturedReview.getRating()).isEqualTo(reviewRegistrationRequest.rating());
        Assertions.assertThat(capturedReview.getUser().getId()).isEqualTo(reviewRegistrationRequest.userId());
        Assertions.assertThat(capturedReview.getLocation().getId()).isEqualTo(reviewRegistrationRequest.locationId());
    }


    @Test
    void willThrowReviewLocationNotExists() {
        User user = new User();
        int userId = 1;
        user.setId(userId);
        Location location =new Location();
        int locationId = 1;
        location.setId(locationId);

        String description = "Nice place, but the weather is bad";
        Integer rating = 3;
        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description, rating, userId, locationId
        );

        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());
       lenient().when(reviewDao.existsReviewByLocationId(locationId)).thenReturn(false);
        assertThatThrownBy(() -> underTest.addReview(reviewRegistrationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        verify(reviewDao,never()).insertReview(any());

    }

    @Test
    void willThrowAddReviewAlreadyExists() {
        User user = new User();
        Location location = new Location();
        int userId = 2;
        int locationId = 2;
        user.setId(userId);
        location.setId(locationId);

        String description = "The best place to visit";
        Integer rating = 5;

        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description,rating, userId, locationId
        );
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));

        when(reviewDao.existsReviewByUserAndLocationId(userId, locationId)).thenReturn(true);

        Assertions.assertThatThrownBy(()->underTest.addReview(reviewRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("review already exists");

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
                .hasMessage("review with id [%s] not found".formatted(id));

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

        Review review = new Review(description, rating, user, location);
        when(reviewDao.selectReviewById(id)).thenReturn(Optional.of(review));

        String newDescription = "Good to visit";
        Integer newRating = 4;

        ReviewUpdateRequest request = new ReviewUpdateRequest(newDescription, newRating);

        underTest.updateReview(id, request);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewDao).updateReview(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();

        Assertions.assertThat(capturedReview.getId()).isNull();
        Assertions.assertThat(capturedReview.getDescription()).isEqualTo(request.description());
        Assertions.assertThat(capturedReview.getRating()).isEqualTo(request.rating());
        Assertions.assertThat(capturedReview.getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(capturedReview.getLocation().getId()).isEqualTo(location.getId());
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

        ReviewUpdateRequest request = new ReviewUpdateRequest (
                review.getDescription(), review.getRating()
        );

        Assertions.assertThatThrownBy(() -> underTest.updateReview(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");


        verify(reviewDao, never()).updateReview(any());


    }
}
