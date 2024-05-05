package com.odyssey.reviews;


import com.odyssey.recommendations.RecommendationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews(){
        return reviewService.getAllReviews();
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable ("reviewId") Integer reviewId){
        return reviewService.getReview(reviewId);

    }

    @GetMapping("/user/{userId}")
    public List<Review>getReviewByUserId(@PathVariable("userId") Integer userId){
        return reviewService.getReviewByUserId(userId);
    }

    @GetMapping("/location/{locationId}")
    public List<Review>getReviewByLocationId(@PathVariable("locationId") Integer locationId){
        return reviewService.getReviewByLocationId(locationId);
    }

    @PostMapping
    public void registerReview(@RequestBody ReviewRegistrationRequest reviewRegistrationRequest){
        reviewService.addReview(reviewRegistrationRequest);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Integer reviewId){
        reviewService.deleteReview(reviewId);
    }

    @PutMapping("/{reviewId}")
    public void updateReview(@PathVariable("reviewId") Integer reviewId,
    @RequestBody ReviewUpdateRequest reviewUpdateRequest){
        reviewService.updateReview(reviewId, reviewUpdateRequest);
    }
}
