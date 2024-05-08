package com.odyssey.tripPlaces;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripPlaceService {

    private final TripPlaceDao tripPlaceDao;
    private final TripDao tripDao;
    private final LocationDao locationDao;

    public TripPlaceService(
            @Qualifier("tripPlaceJPAService") TripPlaceDao tripPlaceDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("locationJPAService") LocationDao locationDao
    ) {
        this.tripPlaceDao = tripPlaceDao;
        this.tripDao = tripDao;
        this.locationDao = locationDao;
    }

    public List<TripPlace> getAllTripPlaces() {
        return tripPlaceDao.selectAllTripPlaces();
    }

    public TripPlace getTripPlaceById(Integer id) {
        return tripPlaceDao.selectTripPlaceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip place with id [%s] not found".formatted(id)));
    }

    public List<TripPlace> getTripPlacesByTripId(Integer tripId) {
        return tripPlaceDao.selectTripPlacesByTripId(tripId);
    }

    public void addTripPlace(TripPlaceRegistrationRequest request) {
        Trip trip = tripDao.selectTripById(request.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(request.tripId())));

        Location place = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        TripPlace tripPlace = new TripPlace(
                trip, place, request.plannedDate(), request.visitOrder()
        );

        if (tripPlaceDao.existsTripPlaceByTripIdAndLocationId(request.tripId(), request.locationId())) {
            throw new DuplicateResourceException("location with id [%s] already added to trip with id [%s]".formatted(request.locationId(), request.tripId()));
        }

        tripPlaceDao.insertTripPlace(tripPlace);
    }

    public boolean deleteTripPlace(Integer id) {
        if (tripPlaceDao.existsTripPlaceById(id)) {
            tripPlaceDao.deleteTripPlaceById(id);
        }
        else {
            throw new ResourceNotFoundException("trip place with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean deleteTripPlacesByTripId(Integer tripId) {
        if (!tripDao.existsTripById(tripId)) {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(tripId));
        }
        tripPlaceDao.deleteTripPlaceByTripId(tripId);
        return false;
    }

}
