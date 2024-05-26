package com.odyssey.controllers;

import com.odyssey.dtos.RecommendationDto;
import com.odyssey.dtos.RecommendationRegistrationRequest;
import com.odyssey.services.RecommendationService;
import com.odyssey.dtos.RecommendationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<RecommendationDto> getAllRecommendations(){
        return recommendationService.getAllRecommendations();
    }

    @GetMapping("/{recommendationId}")
    public RecommendationDto getRecommendationById(@PathVariable("recommendationId") Integer recommendationId){
        return recommendationService.getRecommendation(recommendationId);
    }

    @GetMapping("/user/{userId}")
    public RecommendationDto getRecommendationByUserId(@PathVariable("userId") Integer userId){
        return recommendationService.getRecommendationByUserId(userId);
    }

    @GetMapping("/activity/{activityId}")
    public RecommendationDto getRecommendationByActivityId(@PathVariable("activityId") Integer activityId){
        return recommendationService.getRecommendationByActivityId(activityId);
    }

    @PreAuthorize("hasAuthority('USER') and #request.userId == authentication.principal.id")
    @PostMapping
    public void registerRecommendation(@RequestBody RecommendationRegistrationRequest request){
        recommendationService.addRecommendation(request);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{recommendationId}")
    public void deleteRecommendation(@PathVariable("recommendationId") Integer recommendationId){
        recommendationService.deleteRecommendation(recommendationId);
    }

    @PutMapping("/{recommendationId}")
    public void updateRecommendation(@PathVariable("recommendationId") Integer recommendationId, @RequestBody RecommendationUpdateRequest request){
        recommendationService.updateRecommendation(recommendationId, request);
    }
}
