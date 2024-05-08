package com.odyssey.reviews;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review,Integer> {
    boolean existsReviewById(Integer id);
    boolean existsReviewByUserId(Integer user_id);
    boolean existsReviewByLocationId(Integer location_id);
    boolean existsReviewByUserIdAndLocationId(Integer user_id, Integer location_id);
    List<Review> findReviewByUserId(Integer user_id);
    List<Review> findReviewByLocationId(Integer location_id);


}
