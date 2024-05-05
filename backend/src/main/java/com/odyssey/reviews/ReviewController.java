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

    @GetMapping("/{reviewid}")
    public Review getReviewById(@PathVariable ("reviewid") Integer reviewId){
        return reviewService.getReview(reviewId);

    }

    @GetMapping("/user/{userid}")
    public List<Review>getReviewByUserId(@PathVariable("userid") Integer userId){
        return reviewService.getReviewByUserId(userId);
    }

    @GetMapping("/location/{locationid}")
    public List<Review>getReviewByLocationId(@PathVariable("locationid") Integer locationId){
        return reviewService.getReviewByLocationId(locationId);
    }

    @PostMapping
    public void registerReview(@RequestBody ReviewRegistrationRequest reviewRegistrationRequest){
        reviewService.addReview(reviewRegistrationRequest);
    }

    @DeleteMapping("/{reviewid}")
    public void deleteReview(@PathVariable("reviewid") Integer reviewId){
        reviewService.deleteReview(reviewId);
    }

    @PutMapping("{reviewid}")
    public void updateReview(@PathVariable("reviewid") Integer reviewId,
    @RequestBody ReviewUpdateRequest reviewUpdateRequest){
        reviewService.updateReview(reviewId,reviewUpdateRequest);
    }
}
