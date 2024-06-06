package com.odyssey.repositories;

import com.odyssey.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    boolean existsTripById(Integer id);
    List<Trip> findTripsByUserId(Integer userId);
    boolean existsTripByUserIdAndStartDate(Integer userId, LocalDate startDate);
}
