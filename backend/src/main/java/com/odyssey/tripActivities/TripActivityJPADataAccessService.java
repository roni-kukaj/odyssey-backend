package com.odyssey.tripActivities;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("tripActivityJPAService")
public class TripActivityJPADataAccessService implements TripActivityDao {

    private final TripActivityRepository tripActivityRepository;

    public TripActivityJPADataAccessService(TripActivityRepository tripActivityRepository) {
        this.tripActivityRepository = tripActivityRepository;
    }

    @Override
    public List<TripActivity> selectAllTripActivities() {
        return tripActivityRepository.findAll();
    }

    @Override
    public Optional<TripActivity> selectTripActivityById(Integer id) {
        return tripActivityRepository.findById(id);
    }

    @Override
    public List<TripActivity> selectTripActivitiesByTripId(Integer tripId) {
        return tripActivityRepository.findTripActivitiesByTripId(tripId);
    }

    @Override
    public void insertTripActivity(TripActivity tripActivity) {
        tripActivityRepository.save(tripActivity);
    }

    @Override
    public boolean existsTripActivityById(Integer id) {
        return tripActivityRepository.existsTripActivityById(id);
    }

    @Override
    public boolean existsTripActivityByTripIdAndActivityId(Integer tripId, Integer activityId) {
        return tripActivityRepository.existsTripActivityByTripIdAndActivityId(tripId, activityId);
    }

    @Override
    public void deleteTripActivityById(Integer id) {
        tripActivityRepository.deleteTripActivitiesByTripId(id);
    }

    @Override
    public void deleteTripActivitiesByTripId(Integer tripId) {
        tripActivityRepository.deleteTripActivitiesByTripId(tripId);
    }
}
