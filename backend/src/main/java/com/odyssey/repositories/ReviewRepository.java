package com.odyssey.repositories;

import com.odyssey.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository  extends JpaRepository<Review,Integer> {
    boolean existsReviewById(Integer id);
    boolean existsReviewByUserId(Integer userId);
    boolean existsReviewByLocationId(Integer locationId);
    boolean existsReviewByUserIdAndLocationId(Integer userId, Integer locationId);
    List<Review> findReviewByUserId(Integer userId);
    List<Review> findReviewByLocationId(Integer locationId);

}
