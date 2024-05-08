package com.odyssey.tripActivities;

import java.util.List;
import java.util.Optional;

public interface TripActivityDao {
    List<TripActivity> selectAllTripActivities();
    Optional<TripActivity> selectTripActivityById(Integer id);
    List<TripActivity> selectTripActivitiesByTripId(Integer tripId);
    void insertTripActivity(TripActivity tripActivity);
    boolean existsTripActivityById(Integer id);
    boolean existsTripActivityByTripIdAndActivityId(Integer tripId, Integer activityId);
    void deleteTripActivityById(Integer id);
    void deleteTripActivitiesByTripId(Integer tripId);
}
