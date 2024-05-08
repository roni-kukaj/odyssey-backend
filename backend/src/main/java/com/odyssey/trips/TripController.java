package com.odyssey.trips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public List<TripDto> getTripById() {
        return tripService.getAllTripDto();
    }

}
