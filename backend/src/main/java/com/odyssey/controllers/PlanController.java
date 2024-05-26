package com.odyssey.controllers;

import com.odyssey.dtos.PlanDto;
import com.odyssey.dtos.PlanRegistrationRequest;
import com.odyssey.services.PlanService;
import com.odyssey.dtos.PlanUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<PlanDto> getAllPlans() {
        return planService.getAllPlans();
    }

    @PreAuthorize("hasAuthority('USER') and #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public List<PlanDto> getPlansByUserId(@PathVariable("userId") Integer userId) {
        return planService.getPlansByUserId(userId);
    }

    @GetMapping("/{planId}")
    public PlanDto getPlan(@PathVariable("planId") Integer planId) {
        return planService.getPlan(planId);
    }

    @PreAuthorize("hasAuthority('USER') and #request.userId == authentication.principal.id")
    @PostMapping
    public void registerPlan(@RequestBody PlanRegistrationRequest request) {
        planService.addPlan(request);
    }

    @DeleteMapping("/{planId}")
    public void deletePlan(@PathVariable("planId") Integer planId) {
        planService.deletePlan(planId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/user/{userId}")
    public void deletePlansByUserId(@PathVariable("userId") Integer userId) {
        planService.deletePlansByUserId(userId);
    }

    @PreAuthorize("hasAuthority('USER') and #request.userId == authentication.principal.id")
    @PutMapping("/{planId}")
    public void updatePlan(@PathVariable("planId") Integer planId, @RequestBody PlanUpdateRequest request) {
        planService.updatePlan(planId, request);
    }

}
