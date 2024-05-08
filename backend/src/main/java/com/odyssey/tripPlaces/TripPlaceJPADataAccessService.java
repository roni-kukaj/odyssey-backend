package com.odyssey.tripPlaces;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("tripPlaceJPAService")
public class TripPlaceJPADataAccessService implements TripPlaceDao {

    private final TripPlaceRepository tripPlaceRepository;

    public TripPlaceJPADataAccessService(TripPlaceRepository tripPlaceRepository) {
        this.tripPlaceRepository = tripPlaceRepository;
    }

    @Override
    public List<TripPlace> selectAllTripPlaces() {
        return tripPlaceRepository.findAll();
    }

    @Override
    public Optional<TripPlace> selectTripPlaceById(Integer id) {
        return tripPlaceRepository.findById(id);
    }

    @Override
    public List<TripPlace> selectTripPlacesByTripId(Integer tripId) {
        return tripPlaceRepository.findTripPlacesByTripId(tripId);
    }

    @Override
    public void insertTripPlace(TripPlace tripPlace) {
        tripPlaceRepository.save(tripPlace);
    }

    @Override
    public boolean existsTripPlaceById(Integer id) {
        return tripPlaceRepository.existsTripPlaceById(id);
    }

    @Override
    public boolean existsTripPlaceByTripIdAndLocationId(Integer tripId, Integer locationId) {
        return tripPlaceRepository.existsTripPlaceByTripIdAndLocationId(tripId, locationId);
    }

    @Override
    public void deleteTripPlaceById(Integer id) {
        tripPlaceRepository.deleteById(id);
    }

    @Override
    public void deleteTripPlaceByTripId(Integer tripId) {
        tripPlaceRepository.deleteTripPlacesByTripId(tripId);
    }
}
