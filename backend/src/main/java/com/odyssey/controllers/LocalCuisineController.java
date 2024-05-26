package com.odyssey.controllers;


import com.odyssey.models.LocalCuisine;
import com.odyssey.dtos.LocalCuisineRegistrationDto;
import com.odyssey.services.LocalCuisineService;
import com.odyssey.dtos.LocalCuisineUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void registerLocalCuisine(@ModelAttribute LocalCuisineRegistrationDto dto) {
        localCuisineService.addLocalCuisine(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{localCuisineId}")
    public void deleteLocalCuisine(@PathVariable("localCuisineId") Integer localCuisineId) {
        localCuisineService.deleteLocalCuisine(localCuisineId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{localCuisineId}")
    public void updateLocalCuisine(
            @PathVariable("localCuisineId") Integer localCuisineId,
            @ModelAttribute LocalCuisineUpdateDto dto) {
        localCuisineService.updateLocalCuisine(localCuisineId, dto);
    }



}
