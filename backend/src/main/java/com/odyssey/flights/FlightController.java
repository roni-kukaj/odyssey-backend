package com.odyssey.flights;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public void registerFlight(@RequestBody FlightRegistrationRequest request) {
        flightService.addFlight(request);
    }

    @DeleteMapping("/{flightId}")
    public void deleteFlight(@PathVariable("flightId") Integer flightId) {
        flightService.deleteFlight(flightId);
    }

    @PutMapping("/{flightId}")
    public void updateFlight(@PathVariable("flightId") Integer flightId, @RequestBody FlightUpdateRequest request) {
        flightService.updateFlight(flightId, request);
    }
}
