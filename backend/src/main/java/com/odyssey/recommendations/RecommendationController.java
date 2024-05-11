package com.odyssey.recommendations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<Recommendation> getAllRecommendations(){
        return recommendationService.getAllRecommendations();
    }

    @GetMapping("/{recommendationId}")
    public Recommendation getRecommendationById(@PathVariable("recommendationId") Integer recommendationId){
        return recommendationService.getRecommendation(recommendationId);
    }

    @GetMapping("/user/{userId}")
    public Recommendation getRecommendationByUserId(@PathVariable("userId") Integer userId){
        return recommendationService.getRecommendationByUserId(userId);
    }

    @GetMapping("/activity/{activityId}")
    public Recommendation getRecommendationByActivityId(@PathVariable("activityId") Integer activityId){
        return recommendationService.getRecommendationByActivityId(activityId);
    }

    @PostMapping
    public void registerRecommendation(@RequestBody RecommendationRegistrationRequest request){
        recommendationService.addRecommendation(request);
    }

    @DeleteMapping("/{recommendationId}")
    public void deleteRecommendation(@PathVariable("recommendationId") Integer recommendationId){
        recommendationService.deleteRecommendation(recommendationId);
    }



    @PutMapping("/{recommendationid}")
    public void updateRecommendation(@PathVariable("recommendationid") Integer recommendationid, @RequestBody RecommendationUpdateRequest request){
        recommendationService.updateRecommendation(recommendationid,request);
    }





}
