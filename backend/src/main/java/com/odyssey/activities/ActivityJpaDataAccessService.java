package com.odyssey.activities;

import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRepository;
import com.odyssey.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class ActivityJpaDataAccessService implements ActivityDao{

    private final ActivityRepository activityRepository;

    public ActivityJpaDataAccessService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> selectAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> selectActivityById(Integer id) {
        return activityRepository.findById(id);
    }

    @Override
    public void insertActivity(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public void updateActivity(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public boolean existsActivityById(Integer id) {
        return activityRepository.existsActivityById(id);
    }

    @Override
    public boolean existsActivityByNameAndLocationId(String name, Integer locationId) {
        return activityRepository.existsActivityByNameAndLocationId(name, locationId);
    }

    @Override
    public void deleteActivityById(Integer id) {
        activityRepository.deleteById(id);
    }
}
