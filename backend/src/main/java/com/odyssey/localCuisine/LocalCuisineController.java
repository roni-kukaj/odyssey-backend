package com.odyssey.localCuisine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/localCuisine")
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


    @PostMapping
    public void registerLocalCuisine(@RequestBody LocalCuisineRegistrationRequest request) {
        localCuisineService.addLocalCuisine(request);
    }

    @DeleteMapping("/{localCuisineId}")
    public void deleteLocalCuisine(@PathVariable("localCuisineId") Integer localCuisineId) {
        localCuisineService.deleteLocalCuisine(localCuisineId);
    }

    @PutMapping("/{localCuisineId}")
    public void updateLocalCuisine(@PathVariable("localCuisineId") Integer localCuisineId, @RequestBody LocalCuisineUpdateRequest request) {
        localCuisineService.updateLocalCuisine(localCuisineId, request);
    }


}
