package com.odyssey.reviews;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review,Integer> {
    boolean existsReviewById(Integer id);
    boolean existsReviewByUserId(Integer userId);
    boolean existsReviewByLocationId(Integer locationId);
    boolean existsReviewByUserIdAndLocationId(Integer userId, Integer locationId);
    List<Review> findReviewByUserId(Integer userId);
    List<Review> findReviewByLocationId(Integer locationId);

}
