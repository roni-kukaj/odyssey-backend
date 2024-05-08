package com.odyssey.tripEvents;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("tripEventJPAService")
public class TripEventJPADataAccessService implements TripEventDao {

    private final TripEventRepository tripEventRepository;

    public TripEventJPADataAccessService(TripEventRepository tripEventRepository) {
        this.tripEventRepository = tripEventRepository;
    }

    @Override
    public List<TripEvent> selectAllTripEvents() {
        return tripEventRepository.findAll();
    }

    @Override
    public Optional<TripEvent> selectTripEventById(Integer id) {
        return tripEventRepository.findById(id);
    }

    @Override
    public List<TripEvent> selectTripEventsByTripId(Integer tripId) {
        return tripEventRepository.findTripEventsByTripId(tripId);
    }

    @Override
    public void insertTripEvent(TripEvent tripEvent) {
        tripEventRepository.save(tripEvent);
    }

    @Override
    public boolean existsTripEventById(Integer id) {
        return tripEventRepository.existsTripEventById(id);
    }

    @Override
    public boolean existsTripEventsByTripIdAndEventId(Integer tripId, Integer eventId) {
        return tripEventRepository.existsTripEventByTripIdAndEventId(tripId, eventId);
    }

    @Override
    public void deleteTripEventById(Integer id) {
        tripEventRepository.deleteById(id);
    }

    @Override
    public void deleteTripEventsByTripId(Integer tripId) {
        tripEventRepository.deleteTripEventsByTripId(tripId);
    }
}
