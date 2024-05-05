package com.odyssey.flights;


import java.util.List;
import java.util.Optional;

public interface FlightDao {

    List<Flight> selectAllFlights();
    Optional<Flight> selectFlightById(Integer id);
    List<Flight> selectFlightsByOrigin(Integer origin);
    List<Flight> selectFlightsByDestination(Integer destination);
    void insertFlight(Flight flight);
    void updateFlight(Flight flight);
    boolean existsFlightById(Integer id);
    boolean existsFlightByName(String name);
    void deleteFlightById(Integer id);
}
