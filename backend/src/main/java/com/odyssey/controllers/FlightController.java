package com.odyssey.controllers;

import com.odyssey.models.Flight;
import com.odyssey.dtos.FlightRegistrationRequest;
import com.odyssey.services.FlightService;
import com.odyssey.dtos.FlightUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public List<Flight> getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/{flightId}")
    public Flight getFlightById(@PathVariable("flightId") Integer flightId) {
        return flightService.getFlight(flightId);
    }

    @GetMapping("/origin/{originId}")
    public List<Flight> getFlightsByOriginId(@PathVariable("originId") Integer originId) {
        return flightService.getFlightsByOriginId(originId);
    }

    @GetMapping("/destination/{destinationId}")
    public List<Flight> getFlightsByDestinationId(@PathVariable("destinationId") Integer destinationId) {
        return flightService.getFlightsByDestinationId(destinationId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping
    public void registerFlight(@RequestBody FlightRegistrationRequest request) {
        flightService.addFlight(request);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{flightId}")
    public void deleteFlight(@PathVariable("flightId") Integer flightId) {
        flightService.deleteFlight(flightId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{flightId}")
    public void updateFlight(@PathVariable("flightId") Integer flightId, @RequestBody FlightUpdateRequest request) {
        flightService.updateFlight(flightId, request);
    }
}
