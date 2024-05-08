package com.odyssey.trips;

import java.util.List;
import java.util.Optional;

public interface TripDao {
    List<Trip> selectAllTrips();
    Optional<Trip> selectTripById(Integer id);
    List<Trip> selectTripsByUserId(Integer userId);
    void insertTrip(Trip trip);
    boolean existsTripById(Integer id);
    void deleteTripById(Integer id);
    void updateTrip(Trip trip);
}
