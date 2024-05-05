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

    @GetMapping("/{recommendationid}")
    public Recommendation getRecommendationById(@PathVariable("recommendationid") Integer recommendationid){
        return recommendationService.getRecommendation(recommendationid);
    }

    @GetMapping("/user/{userid}")
    public Optional<Recommendation> getRecommendationByUserId(@PathVariable("userid") Integer userid){
        return recommendationService.getRecommendationByUserId(userid);
    }

    @GetMapping("/activity/{activityid}")
    public Optional<Recommendation> getRecommendationByActivityId(@PathVariable("activityid") Integer activityid){
        return recommendationService.getRecommendationByActivityId(activityid);
    }

    @PostMapping
    public void registerRecommendation(@RequestBody RecommendationRegistrationRequest request){
        recommendationService.addRecommendation(request);
    }

    @DeleteMapping("/{recommendationid}")
    public void deleteRecommendation(@PathVariable("recommendationid") Integer recommendationid){
        recommendationService.deleteRecommendation(recommendationid);
    }



    @PutMapping("/{recommendationid}")
    public void updateRecommendation(@PathVariable("recommendationid") Integer recommendationid, @RequestBody RecommendationUpdateRequest request){
        recommendationService.updateRecommendation(recommendationid,request);
    }





}
