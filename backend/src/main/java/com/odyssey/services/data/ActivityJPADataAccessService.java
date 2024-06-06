package com.odyssey.services.data;

import com.odyssey.repositories.ActivityRepository;
import com.odyssey.daos.ActivityDao;
import com.odyssey.models.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("activityJPAService")
public class ActivityJPADataAccessService implements ActivityDao {

    private final ActivityRepository activityRepository;

    public ActivityJPADataAccessService(ActivityRepository activityRepository) {
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
    public List<Activity> selectActivitiesByLocationId(Integer locationId) {
        return activityRepository.findByLocationId(locationId);
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
