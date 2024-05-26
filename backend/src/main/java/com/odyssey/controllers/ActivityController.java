package com.odyssey.controllers;

import com.odyssey.models.Activity;
import com.odyssey.dtos.ActivityRegistrationRequest;
import com.odyssey.services.ActivityService;
import com.odyssey.dtos.ActivityUpdateRequest;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{activityId}")
    public Activity getActivityById(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivity(activityId);
    }

    @GetMapping("/location/{locationId}")
    public List<Activity> getActivitiesByLocationId(@PathVariable("locationId") Integer locationId) {
        return activityService.getActivitiesByLocationId(locationId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping
    public void registerActivity(@RequestBody ActivityRegistrationRequest request) {
        activityService.addActivity(request);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{activityId}")
    public void deleteActivity(@PathVariable("activityId") Integer activityId) {
        activityService.deleteActivity(activityId);
    }

    @Secured({"ADMIN", "MAINADMIN"})
    @PutMapping("/{activityId}")
    public void updateActivity(@PathVariable("activityId") Integer activityId, @RequestBody ActivityUpdateRequest request) {
        activityService.updateActivity(activityId, request);
    }

}
