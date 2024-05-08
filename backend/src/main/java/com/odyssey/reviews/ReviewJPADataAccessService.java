package com.odyssey.reviews;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("reviewJPAService")
public class ReviewJPADataAccessService implements ReviewDao{

    private final ReviewRepository reviewRepository;

    public ReviewJPADataAccessService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> selectAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> selectReviewById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> selectReviewByUserId(Integer user_id) {
        return reviewRepository.findReviewByUserId(user_id);
    }

    @Override
    public List<Review> selectReviewByLocationId(Integer location_id) {
        return reviewRepository.findReviewByLocationId(location_id);
    }

    @Override
    public void insertReview(Review review) {
         reviewRepository.save(review);

    }

    @Override
    public void updateReview(Review review) {
        reviewRepository.save(review);

    }

    @Override
    public void deleteReviewById(Integer id) {
        reviewRepository.deleteById(id);

    }

    @Override
    public boolean existsReviewById(Integer id) {
        return reviewRepository.existsReviewById(id);
    }

    @Override
    public boolean existsReviewByUserId(Integer user_id) {
        return reviewRepository.existsReviewByUserId(user_id);
    }

    @Override
    public boolean existsReviewByLocationId(Integer location_id) {
        return reviewRepository.existsReviewByLocationId(location_id);
    }

    @Override
    public boolean existsReviewByUserAndLocationId(Integer user_id, Integer location_id) {
        return reviewRepository.existsReviewByUserIdAndLocationId(user_id,location_id);
    }
}
