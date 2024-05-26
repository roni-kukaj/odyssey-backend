package com.odyssey.controllers;

import com.odyssey.models.Hotel;
import com.odyssey.dtos.HotelRegistrationRequest;
import com.odyssey.services.HotelService;
import com.odyssey.dtos.HotelUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{hotelId}")
    public Hotel getHotelById(@PathVariable("hotelId") Integer hotelId) {
        return hotelService.getHotel(hotelId);
    }

    @GetMapping("/location/{locationId}")
    public List<Hotel> getHotelsByLocationId(@PathVariable("locationId") Integer locationId) {
        return hotelService.getHotelsByLocationId(locationId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping
    public void registerHotel(@RequestBody HotelRegistrationRequest request) {
        hotelService.addHotel(request);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{hotelId}")
    public void deleteHotel(@PathVariable("hotelId") Integer hotelId) {
        hotelService.deleteHotel(hotelId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{hotelId}")
    public void updateHotel(@PathVariable("hotelId") Integer hotelId, @RequestBody HotelUpdateRequest request) {
        hotelService.updateHotel(hotelId, request);
    }

}
