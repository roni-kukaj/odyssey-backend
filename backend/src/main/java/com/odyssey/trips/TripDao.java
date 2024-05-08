package com.odyssey.trips;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripDao {
    List<Trip> selectAllTrips();
    Optional<Trip> selectTripById(Integer id);
    List<Trip> selectTripsByUserId(Integer userId);
    Optional<Trip> selectTripByUserIdAndStartDateAndEndDate(Integer userId, LocalDate startDate, LocalDate endDate);
    void insertTrip(Trip trip);
    boolean existsTripById(Integer id);
    boolean existsTripByUserIdAndStartDateAndEndDate(Integer userId, LocalDate startDate, LocalDate endDate);
    void deleteTripById(Integer id);
    void updateTrip(Trip trip);
}
