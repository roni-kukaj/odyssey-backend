package com.odyssey.trips;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/{tripId}")
    public Trip getTrip(@PathVariable("tripId") Integer tripId) {
        return tripService.getTrip(tripId);
    }

    @GetMapping("/user/{userId}")
    public List<Trip> getTripsByUserId(@PathVariable("userId") Integer userId) {
        return tripService.getTripsByUserId(userId);
    }

    @PostMapping
    public void registerTrip(@RequestBody TripRegistrationRequest request) {
        tripService.addTrip(request);
    }

    @DeleteMapping("/{tripId}")
    public void deleteTrip(@PathVariable("tripId") Integer tripId) {
        tripService.deleteTripById(tripId);
    }

    @PutMapping("/{tripId}")
    public void updateTrip(@PathVariable("tripId") Integer tripId, @RequestBody TripUpdateRequest request) {
        tripService.updateTrip(tripId, request);
    }
}
