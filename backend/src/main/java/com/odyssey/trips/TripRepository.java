package com.odyssey.trips;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    boolean existsTripById(Integer id);
    List<Trip> findTripsByUserId(Integer userId);
    boolean existsTripByUserIdAndStartDate(Integer userId, LocalDate startDate);
}