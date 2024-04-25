package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Optional<Location>> getLocationById(@PathVariable("locationId") Integer locationId) {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }


    @PostMapping
    public ResponseEntity<Void> createLocation(@RequestBody Location location) {
        locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("locationId") Integer locationId) {
        boolean deleted = locationService.deleteLocation(locationId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<Void> updateLocation(@PathVariable("locationId") Integer locationId, @RequestBody Location location) {
        location.setId(locationId);
        boolean updated = locationService.updateLocation(location);
        if (updated) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
