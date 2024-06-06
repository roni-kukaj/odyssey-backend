package com.odyssey.daos;

import com.odyssey.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    List<Review> selectAllReviews();
    Optional<Review>selectReviewById(Integer id);
    List<Review>selectReviewByUserId(Integer userId);
    List<Review>selectReviewByLocationId(Integer locationId);
    void insertReview(Review review);
    void updateReview(Review review);
    void deleteReviewById(Integer id);
    boolean existsReviewById(Integer id);
    boolean existsReviewByUserId(Integer userId);
    boolean existsReviewByLocationId(Integer locationId);
    boolean existsReviewByUserAndLocationId(Integer userId, Integer locationId);

}
