package com.odyssey.trips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public List<TripGetDto> getAllTrips() {
        return tripService.getAllTripDto();
    }

    @GetMapping("/{tripId}")
    public TripGetDto getTripById(@PathVariable("tripId") Integer tripId) {
        return tripService.getTripById(tripId);
    }

    @GetMapping("/user/{userId}")
    public List<TripGetDto> getTripsByUserId(@PathVariable("userId") Integer userId) {
        return tripService.getTripsByUserId(userId);
    }

    @PostMapping
    public void registerTrip(@RequestBody TripRegistrationRequest request) {
        tripService.addTrip(request);
    }

    @DeleteMapping("/{tripId}")
    public void deleteTripById(@PathVariable("tripId") Integer tripId) {
        tripService.deleteTrip(tripId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteTripsByUserId(@PathVariable("userId") Integer userId) {
        tripService.deleteAllUserTrips(userId);
    }


//    @PutMapping("/{tripId}") TODO
}
