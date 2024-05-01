package com.odyssey.activities;

import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
import com.odyssey.locations.LocationService;
import com.odyssey.locations.LocationUpdateRequest;
import com.odyssey.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @PostMapping
    public void registerActivity(@RequestBody ActivityRegistrationRequest request) {
        activityService.addActivity(request);
    }

    @DeleteMapping("/{activityId}")
    public void deleteActivity(@PathVariable("activityId") Integer activityId) {
        activityService.deleteActivity(activityId);
    }

    @PutMapping("/{activityId}")
    public void updateActivity(@PathVariable("activityId") Integer activityId, @RequestBody ActivityUpdateRequest request) {
        activityService.updateActivity(activityId, request);
    }

}
