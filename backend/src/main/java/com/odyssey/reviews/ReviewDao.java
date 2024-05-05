package com.odyssey.reviews;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    List<Review> selectAllReviews();
    Optional<Review>selectReviewById(Integer id);

    List<Review>selectReviewByUserId(Integer user_id);
    List<Review>selectReviewByLocationId(Integer location_id);
    void insertReview(Review review);
    void updateReview(Review review);
    void deleteReviewById(Integer id);
    boolean existsReviewById(Integer id);
    boolean existsReviewByUserId(Integer user_id);
    boolean existsReviewByLocationId(Integer location_id);
    boolean existsReviewByUserAndLocationId(Integer user_id, Integer location_id);

}
