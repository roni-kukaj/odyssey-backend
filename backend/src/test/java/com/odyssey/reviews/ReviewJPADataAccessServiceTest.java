package com.odyssey.reviews;

import com.odyssey.activities.ActivityJPADataAccessService;
import com.odyssey.locations.Location;
import com.odyssey.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ReviewJPADataAccessServiceTest {

    private ReviewJPADataAccessService reviewJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        reviewJPADataAccessService = new ReviewJPADataAccessService(reviewRepository);
    }



    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllReviews() {
        reviewJPADataAccessService.selectAllReviews();
        verify(reviewRepository).findAll();
    }

    @Test
    void selectReviewById() {
        int id = 1;
        reviewJPADataAccessService.selectReviewById(id);
        verify(reviewRepository).findById(id);
    }

    @Test
    void selectReviewByUserId() {
        int id = 1;
        reviewJPADataAccessService.selectReviewByUserId(id);
        verify(reviewRepository).findReviewByUserId(id);
    }

    @Test
    void selectReviewByLocationId() {
        int id = 1;
        reviewJPADataAccessService.selectReviewByLocationId(id);
        verify(reviewRepository).findReviewByLocationId(id);
    }


    @Test
    void insertReview() {
        Review review = new Review("Very good place to visit",4,new User(),new Location());
        reviewJPADataAccessService.insertReview(review);
        verify(reviewRepository).save(review);
    }

    @Test
    void existsReviewById() {
        int id = 1;
        reviewJPADataAccessService.existsReviewById(id);
        verify(reviewRepository).existsReviewById(id);
    }

    @Test
    void existsReviewByUserId() {
        int id = 1;
        reviewJPADataAccessService.existsReviewByUserId(id);
        verify(reviewRepository).existsReviewByUserId(id);

    }

    @Test
    void existsReviewByLocationId() {
        int id = 1;
        reviewJPADataAccessService.existsReviewByLocationId(id);
        verify(reviewRepository).existsReviewByLocationId(id);
    }

    @Test
    void existsReviewByUserAndLocationId() {
        int user_id = 1;
        int location_id = 1;
        reviewJPADataAccessService.existsReviewByUserAndLocationId(user_id,location_id);
        verify(reviewRepository).existsReviewByUserIdAndLocationId(user_id,location_id);
    }


    @Test
    void updateReview() {
        Review review = new Review("Good place to visit",4,new User(),new Location());
        reviewJPADataAccessService.updateReview(review);
        verify(reviewRepository).save(review);
    }

    @Test
    void deleteReviewById() {
        int id = 6;
        reviewJPADataAccessService.deleteReviewById(id);
        verify(reviewRepository).deleteById(id);
    }
}
