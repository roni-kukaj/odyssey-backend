package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public Location getLocationById(@PathVariable("locationId") Integer locationId) {
        return locationService.getLocation(locationId);
    }


    @PostMapping
    public void registerLocation(@RequestBody LocationRegistrationRequest request) {
        locationService.addLocation(request);
    }

    @DeleteMapping("/{locationId}")
    public void deleteLocation(@PathVariable("locationId") Integer locationId) {
        locationService.deleteLocation(locationId);
    }

    @PutMapping("/{locationId}")
    public void updateLocation(@PathVariable("locationId") Integer locationId, @RequestBody LocationUpdateRequest request) {
        locationService.updateLocation(locationId, request);
    }
}
