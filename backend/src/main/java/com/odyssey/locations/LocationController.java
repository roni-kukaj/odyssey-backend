package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void registerLocation(
                                 @RequestParam("city") String city,
                                 @RequestParam("country") String country,
                                 @RequestPart("file") MultipartFile file
    ) {
        LocationRegistrationDto locationRegistrationDto = new LocationRegistrationDto(city, country, file);
        locationService.addLocation(locationRegistrationDto);
    }

    @DeleteMapping("/{locationId}")
    public void deleteLocation(@PathVariable("locationId") Integer locationId) {
        locationService.deleteLocation(locationId);
    }

    @PutMapping("/{locationId}")
    public void updateLocationInformation(
            @PathVariable("locationId") Integer locationId,
            @RequestBody LocationUpdateInformationDto dto
    ) {
        locationService.updateLocationInformation(locationId, dto);
    }

    @PutMapping(value = "/{locationId}/picture", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateLocationPicture(
            @PathVariable("locationId") Integer locationId,
            @RequestPart("file") MultipartFile file
    ) {
        locationService.updateLocationPicture(locationId, file);
    }
}
