package com.odyssey.services.data;

import com.odyssey.daos.ReviewDao;
import com.odyssey.models.Review;
import com.odyssey.repositories.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("reviewJPAService")
public class ReviewJPADataAccessService implements ReviewDao {

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
    public List<Review> selectReviewByUserId(Integer userId) {
        return reviewRepository.findReviewByUserId(userId);
    }

    @Override
    public List<Review> selectReviewByLocationId(Integer locationId) {
        return reviewRepository.findReviewByLocationId(locationId);
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
    public boolean existsReviewByUserId(Integer userId) {
        return reviewRepository.existsReviewByUserId(userId);
    }

    @Override
    public boolean existsReviewByLocationId(Integer locationId) {
        return reviewRepository.existsReviewByLocationId(locationId);
    }


    public boolean existsReviewByUserAndLocationId(Integer userId, Integer locationId) {
        return reviewRepository.existsReviewByUserIdAndLocationId(userId,locationId);

    }
}
