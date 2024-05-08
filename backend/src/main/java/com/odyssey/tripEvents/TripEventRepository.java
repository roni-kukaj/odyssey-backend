package com.odyssey.tripEvents;

import com.odyssey.events.Event;
import com.odyssey.trips.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripEventRepository extends JpaRepository<TripEvent, Integer> {
    boolean existsTripEventById(Integer id);
    boolean existsTripEventByTripIdAndEventId(Integer tripId, Integer activityId);
    List<TripEvent> findTripEventsByTripId(Integer tripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TripEvent t WHERE t.trip.id = :tripId")
    void deleteTripEventsByTripId(Integer tripId);
}
