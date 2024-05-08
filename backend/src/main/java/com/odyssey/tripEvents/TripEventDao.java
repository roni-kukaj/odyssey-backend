package com.odyssey.tripEvents;

import com.odyssey.tripActivities.TripActivity;

import java.util.List;
import java.util.Optional;

public interface TripEventDao {
    List<TripEvent> selectAllTripEvents();
    Optional<TripEvent> selectTripEventById(Integer id);
    List<TripEvent> selectTripEventsByTripId(Integer tripId);
    void insertTripEvent(TripEvent tripEvent);
    boolean existsTripEventById(Integer id);
    boolean existsTripEventsByTripIdAndEventId(Integer tripId, Integer eventId);
    void deleteTripEventById(Integer id);
    void deleteTripEventsByTripId(Integer tripId);
}
