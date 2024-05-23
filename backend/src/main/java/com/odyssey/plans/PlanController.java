package com.odyssey.plans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<Plan> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/user/{userId}")
    public List<Plan> getPlansByUserId(@PathVariable("userId") Integer userId) {
        return planService.getPlansByUserId(userId);
    }

    @GetMapping("/{planId}")
    public Plan getPlan(@PathVariable("planId") Integer planId) {
        return planService.getPlan(planId);
    }

    @PostMapping
    public void registerPlan(@RequestBody PlanRegistrationRequest request) {
        planService.addPlan(request);
    }

    @DeleteMapping("/{planId}")
    public void deletePlan(@PathVariable("planId") Integer planId) {
        planService.deletePlan(planId);
    }

    @DeleteMapping("/user/{userId}")
    public void deletePlansByUserId(@PathVariable("userId") Integer userId) {
        planService.deletePlansByUserId(userId);
    }

    @PutMapping("/{planId}")
    public void updatePlan(@PathVariable("planId") Integer planId, @RequestBody PlanUpdateRequest request) {
        planService.updatePlan(planId, request);
    }

}
