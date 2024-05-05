package com.odyssey.flights;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("flightJPAService")
public class FlightJPADataAccessService implements FlightDao{

    private final FlightRepository flightRepository;
    public FlightJPADataAccessService(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> selectAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Optional<Flight> selectFlightById(Integer id) {
        return flightRepository.findById(id);
    }

    @Override
    public List<Flight> selectFlightsByOrigin(Integer origin) {
        return flightRepository.findByOriginId(origin);
    }

    @Override
    public List<Flight> selectFlightsByDestination(Integer destination) {
        return flightRepository.findByDestinationId(destination);
    }

    @Override
    public void insertFlight(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public void updateFlight(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public boolean existsFlightById(Integer id) {
        return flightRepository.existsFlightById(id);
    }

    @Override
    public boolean existsFlightByName(String name) {
       return flightRepository.existsFlightByName(name);
    }



    @Override
    public void deleteFlightById(Integer id) {
        flightRepository.deleteById(id);
    }
}
