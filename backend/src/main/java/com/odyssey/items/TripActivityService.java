package com.odyssey.tripActivities;

import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityDao;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.tripPlaces.TripPlace;
import com.odyssey.tripPlaces.TripPlaceRegistrationRequest;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripActivityService {

    private final TripActivityDao tripActivityDao;
    private final TripDao tripDao;
    private final ActivityDao activityDao;

    public TripActivityService(
            @Qualifier("tripActivityJPAService") TripActivityDao tripActivityDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("activityJPAService") ActivityDao activityDao
    ) {
        this.tripActivityDao = tripActivityDao;
        this.tripDao = tripDao;
        this.activityDao = activityDao;
    }

    public List<TripActivity> getAllTripActivities() {
        return tripActivityDao.selectAllTripActivities();
    }

    public TripActivity getTripActivityById(Integer id) {
        return tripActivityDao.selectTripActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip activity with id [%s] not found".formatted(id)));
    }

    public List<TripActivity> getTripActivitiesByTripId(Integer tripId) {
        return tripActivityDao.selectTripActivitiesByTripId(tripId);
    }

    public void addTripActivity(TripActivityRegistrationRequest request) {
        Trip trip = tripDao.selectTripById(request.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(request.tripId())));

        Activity activity = activityDao.selectActivityById(request.activityId())
                .orElseThrow(() -> new ResourceNotFoundException("activity with id [%s] not found".formatted(request.activityId())));

        TripActivity tripActivity = new TripActivity(
                trip, activity, request.plannedDate(), request.visitOrder()
        );

        if (tripActivityDao.existsTripActivityByTripIdAndActivityId(request.tripId(), request.activityId())) {
            throw new DuplicateResourceException("activity with id [%s] already added to trip with id [%s]".formatted(request.activityId(), request.tripId()));
        }

        tripActivityDao.insertTripActivity(tripActivity);
    }

    public boolean deleteTripActivity(Integer id) {
        if (tripActivityDao.existsTripActivityById(id)) {
            tripActivityDao.deleteTripActivityById(id);
        } else {
            throw new ResourceNotFoundException("trip activity with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean deleteTripActivitiesByTripId(Integer tripId) {
        if (!tripDao.existsTripById(tripId)) {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(tripId));
        }
        tripActivityDao.deleteTripActivitiesByTripId(tripId);
        return false;
    }
}