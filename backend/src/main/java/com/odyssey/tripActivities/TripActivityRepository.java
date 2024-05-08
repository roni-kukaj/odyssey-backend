package com.odyssey.tripActivities;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripActivityRepository extends JpaRepository<TripActivity, Integer> {
    boolean existsTripActivityById(Integer id);
    boolean existsTripActivityByTripIdAndActivityId(Integer tripId, Integer activityId);
    List<TripActivity> findTripActivitiesByTripId(Integer tripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TripActivity t WHERE t.trip.id = :tripId")
    void deleteTripActivitiesByTripId(Integer tripId);
}
