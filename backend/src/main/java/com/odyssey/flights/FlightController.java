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
    public List<Flight>getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/{flightId}")
    public Flight getFlightById(@PathVariable("flightId") Integer flightId) {
        return flightService.getFlight(flightId);
    }

    @GetMapping("/location/{origin}")
    public List<Flight> getFlightsByOriginId(@PathVariable("origin") Integer origin) {
        return flightService.getFlightsByOriginId(origin);
    }

    @GetMapping("/location/{destination}")
    public List<Flight> getFlightsByDestinationId(@PathVariable("destination") Integer destination) {
        return flightService.getFlightsByDestinationId(destination);
    }

    @PostMapping
    public void registerFlight(@RequestBody FlightRegistrationRequest request) {
        flightService.addFlight(request);
    }

    @DeleteMapping("/{flightId}")
    public void deleteFlight(@PathVariable("flightId") Integer flightId) {
        flightService.deleteFlight(flightId);
    }

    @PutMapping("/{FlightId}")
    public void flightFlight(@PathVariable("flightId") Integer flightId, @RequestBody FlightUpdateRequest request) {
        flightService.updateFlight(flightId, request);
    }

}
