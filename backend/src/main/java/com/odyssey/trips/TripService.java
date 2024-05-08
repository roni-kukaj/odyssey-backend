package com.odyssey.trips;

import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.tripActivities.TripActivity;
import com.odyssey.tripActivities.TripActivityDao;
import com.odyssey.tripEvents.TripEvent;
import com.odyssey.tripEvents.TripEventDao;
import com.odyssey.tripItems.TripItem;
import com.odyssey.tripItems.TripItemDao;
import com.odyssey.tripPlaces.TripPlace;
import com.odyssey.tripPlaces.TripPlaceDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripService {

    private final TripDao tripDao;
    private final TripItemDao tripItemDao;
    private final TripPlaceDao tripPlaceDao;
    private final TripActivityDao tripActivityDao;
    private final TripEventDao tripEventDao;

    public TripService(
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("tripItemJPAService") TripItemDao tripItemDao,
            @Qualifier("tripPlaceJPAService") TripPlaceDao tripPlaceDao,
            @Qualifier("tripActivityJPAService") TripActivityDao tripActivityDao,
            @Qualifier("tripEventJPAService") TripEventDao tripEventDao
    ) {
        this.tripDao = tripDao;
        this.tripItemDao = tripItemDao;
        this.tripPlaceDao = tripPlaceDao;
        this.tripActivityDao = tripActivityDao;
        this.tripEventDao = tripEventDao;
    }

    public Trip getTrip(Integer id){
        return tripDao.selectTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesazhi"));
    }

    public List<Trip> getAllTrips() {
        return tripDao.selectAllTrips();
    }

    public List<TripDto> getAllTripDto() {
        List<TripDto> allTrips = new ArrayList<>();
        List<Trip> trips = tripDao.selectAllTrips();

        for (Trip trip: trips) {
            List<TripItem> tripItems = tripItemDao.selectTripItemsByTripId(trip.getId());
            List<TripPlace> tripPlaces = tripPlaceDao.selectTripPlacesByTripId(trip.getId());
            List<TripActivity> tripActivities = tripActivityDao.selectTripActivitiesByTripId(trip.getId());
            List<TripEvent> tripEvents = tripEventDao.selectTripEventsByTripId(trip.getId());

            allTrips.add(
                    new TripDto(
                            trip.getId(),
                            trip.getUser(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            tripItems,
                            tripPlaces,
                            tripActivities,
                            tripEvents
                    )
            );
        }

        return allTrips;
    }

}
