package com.odyssey.tripPlaces;

import com.odyssey.trips.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripPlaceRepository extends JpaRepository<TripPlace, Integer> {
    boolean existsTripPlaceById(Integer id);
    boolean existsTripPlaceByTripIdAndLocationId(Integer tripId, Integer locationId);
    List<TripPlace> findTripPlacesByTripId(Integer tripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TripPlace t WHERE t.trip.id = :tripId")
    void deleteTripPlacesByTripId(Integer tripId);
}