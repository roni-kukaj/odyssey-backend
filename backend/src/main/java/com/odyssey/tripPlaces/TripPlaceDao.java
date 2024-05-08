package com.odyssey.tripPlaces;

import java.util.List;
import java.util.Optional;

public interface TripPlaceDao {
    List<TripPlace> selectAllTripPlaces();
    Optional<TripPlace> selectTripPlaceById(Integer id);
    List<TripPlace> selectTripPlacesByTripId(Integer tripId);
    void insertTripPlace(TripPlace tripPlace);
    boolean existsTripPlaceById(Integer id);
    boolean existsTripPlaceByTripIdAndLocationId(Integer tripId, Integer locationId);
    void deleteTripPlaceById(Integer id);
    void deleteTripPlaceByTripId(Integer tripId);
}
