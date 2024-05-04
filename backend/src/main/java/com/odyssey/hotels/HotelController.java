package com.odyssey.hotels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<Hotel> getALlHotels() {
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

    @PostMapping
    public void registerHotel(@RequestBody HotelRegistrationRequest request) {
        hotelService.addHotel(request);
    }

    @DeleteMapping("/{hotelId}")
    public void deleteHotel(@PathVariable("hotelId") Integer hotelId) {
        hotelService.deleteHotel(hotelId);
    }

    @PutMapping("/{hotelId}")
    public void updateHotel(@PathVariable("hotelId") Integer hotelId, @RequestBody HotelUpdateRequest request) {
        hotelService.updateHotel(hotelId, request);
    }

}
