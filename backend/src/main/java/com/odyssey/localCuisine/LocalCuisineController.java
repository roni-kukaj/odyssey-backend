package com.odyssey.localCuisine;


import com.odyssey.locations.LocationRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/local-cuisine")
public class LocalCuisineController {

    @Autowired
    private LocalCuisineService localCuisineService;

    @GetMapping
    public List<LocalCuisine> getAllLocalCuisines() {
        return localCuisineService.getAllLocalCuisines();
    }

    @GetMapping("/{localCuisineId}")
    public LocalCuisine getLocalCuisineById(@PathVariable("localCuisineId") Integer localCuisineId) {
        return localCuisineService.getLocalCuisine(localCuisineId);
    }

    @GetMapping("/location/{locationId}")
    public List<LocalCuisine> getLocalCuisinesByLocationId(@PathVariable("locationId") Integer locationId) {
        return localCuisineService.getLocalCuisinesByLocationId(locationId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void registerLocalCuisine(@ModelAttribute LocalCuisineRegistrationDto dto) {
        localCuisineService.addLocalCuisine(dto);
    }

    @DeleteMapping("/{localCuisineId}")
    public void deleteLocalCuisine(@PathVariable("localCuisineId") Integer localCuisineId) {
        localCuisineService.deleteLocalCuisine(localCuisineId);
    }

    @PutMapping("/{localCuisineId}")
    public void updateLocalCuisine(
            @PathVariable("localCuisineId") Integer localCuisineId,
            @ModelAttribute LocalCuisineUpdateDto dto) {
        localCuisineService.updateLocalCuisine(localCuisineId, dto);
    }



}
