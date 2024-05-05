package com.odyssey.flights;

import com.odyssey.hotels.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    boolean existsFlightById(Integer id);
    boolean existsFlightByName(String name);
    List<Flight> findByOriginId(Integer origin);
    List<Flight> findByDestinationId(Integer destination);

}
