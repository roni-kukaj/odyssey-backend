package com.odyssey.trips;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    boolean existsTripById(Integer id);
    List<Trip> findTripsByUserId(Integer userId);
}
