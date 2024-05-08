package com.odyssey.tripEvents;

import com.odyssey.events.Event;
import com.odyssey.events.EventDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.tripActivities.TripActivity;
import com.odyssey.tripActivities.TripActivityGetDto;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripEventService {

    private final TripEventDao tripEventDao;
    private final TripDao tripDao;
    private final EventDao eventDao;

    public TripEventService (
            @Qualifier("tripEventJPAService") TripEventDao tripEventDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("eventJPAService") EventDao eventDao
    ) {
        this.tripEventDao = tripEventDao;
        this.tripDao = tripDao;
        this.eventDao = eventDao;
    }

    public List<TripEvent> getAllTripEvents() {
        return tripEventDao.selectAllTripEvents();
    }

    public TripEvent getTripEventById(Integer id) {
        return tripEventDao.selectTripEventById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip event with id [%s] not found".formatted(id)));
    }

    public List<TripEvent> getTripEventsByTripId(Integer tripId) {
        return tripEventDao.selectTripEventsByTripId(tripId);
    }

    public void addTripActivity(TripEventRegistrationRequest request) {
        Trip trip = tripDao.selectTripById(request.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(request.tripId())));

        Event event = eventDao.selectEventById(request.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("event with id [%s] not found".formatted(request.eventId())));

        TripEvent tripEvent = new TripEvent(
                trip, event, request.plannedDate(), request.visitOrder()
        );

        if (tripEventDao.existsTripEventsByTripIdAndEventId(request.tripId(), request.eventId())) {
            throw new DuplicateResourceException("event with id [%s] already added to trip with id [%s]".formatted(request.eventId(), request.tripId()));
        }

        tripEventDao.insertTripEvent(tripEvent);
    }

    public boolean deleteTripEvent(Integer id) {
        if (tripEventDao.existsTripEventById(id)) {
            tripEventDao.deleteTripEventById(id);
        }
        else {
            throw new ResourceNotFoundException("trip event with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean deleteTripEventsByTripId(Integer tripId) {
        if (!tripDao.existsTripById(tripId)) {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(tripId));
        }
        tripEventDao.deleteTripEventsByTripId(tripId);
        return false;
    }

    public List<TripEventGetDto> selectTripEventGetDtoByTripId(Integer tripId) {
        List<TripEvent> tripEvents = getTripEventsByTripId(tripId);
        List<TripEventGetDto> tripEventGetDto = new ArrayList<>();
        for (TripEvent tripEvent: tripEvents) {
            tripEventGetDto.add(
                    new TripEventGetDto (
                            tripEvent.getId(),
                            tripEvent.getEvent(),
                            tripEvent.getPlannedDate(),
                            tripEvent.getVisitOrder()
                    )
            );
        }
        return tripEventGetDto;
    }

}