package com.odyssey.services.data;


import com.odyssey.daos.FlightDao;
import com.odyssey.repositories.FlightRepository;
import com.odyssey.models.Flight;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("flightJPAService")
public class FlightJPADataAccessService implements FlightDao {

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
    public List<Flight> selectFlightsByOriginId(Integer originId) {
        return flightRepository.findByOriginId(originId);
    }

    @Override
    public List<Flight> selectFlightsByDestinationId(Integer destinationId) {
        return flightRepository.findByDestinationId(destinationId);
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
