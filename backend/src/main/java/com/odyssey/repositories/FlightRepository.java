package com.odyssey.repositories;

import com.odyssey.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    boolean existsFlightById(Integer id);
    boolean existsFlightByName(String name);
    List<Flight> findByOriginId(Integer originId);
    List<Flight> findByDestinationId(Integer destinationId);

}
