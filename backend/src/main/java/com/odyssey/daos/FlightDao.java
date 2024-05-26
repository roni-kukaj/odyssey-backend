package com.odyssey.daos;


import com.odyssey.models.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightDao {
    List<Flight> selectAllFlights();
    Optional<Flight> selectFlightById(Integer id);
    List<Flight> selectFlightsByOriginId(Integer originId);
    List<Flight> selectFlightsByDestinationId(Integer destinationId);
    void insertFlight(Flight flight);
    void updateFlight(Flight flight);
    boolean existsFlightById(Integer id);
    boolean existsFlightByName(String name);
    void deleteFlightById(Integer id);
}
