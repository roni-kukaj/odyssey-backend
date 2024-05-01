package com.odyssey.activities;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityDao activityDao;
    private final LocationDao locationDao;

    public ActivityService(@Qualifier("activityJPAService") ActivityDao activityDao, @Qualifier("locationJPAService") LocationDao locationDao) {
        this.activityDao = activityDao;
        this.locationDao = locationDao;
    }

    public List<Activity> getAllActivities() {
        return activityDao.selectAllActivities();
    }

    public Activity getActivity(Integer id) {
        return activityDao.selectActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("activity with id [%s] not found".formatted(id)));
    }

    public void addActivity(ActivityRegistrationRequest request) {
        if (activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("Activity already exists with the given name and location.");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location with id [%s] not found".formatted(request.locationId())));

        Activity activity = new Activity(
                null, // Assuming ID is auto-generated.
                request.name(),
                request.description(),
                request.cost(),
                request.duration(),
                location
        );

        activityDao.insertActivity(activity);
    }

    public boolean deleteActivity(Integer id) {
        if (activityDao.existsActivityById(id)) {
            activityDao.deleteActivityById(id);
        } else {
            throw new ResourceNotFoundException("activity with id [%s] not found".formatted(id));
        }
        return false;
    }

    public void updateActivity(Integer id, ActivityUpdateRequest request) {
        Activity existingActivity = activityDao.selectActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id [%s] not found".formatted(id)));

        if (activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId()) &&
                !existingActivity.getLocation().getId().equals(request.locationId()) ||
                !existingActivity.getName().equals(request.name())) {
            throw new DuplicateResourceException("Another activity with the same name and location already exists.");
        }

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location with id [%s] not found".formatted(request.locationId())));

        existingActivity.setName(request.name());
        existingActivity.setDescription(request.description());
        existingActivity.setCost(request.cost());
        existingActivity.setDuration(request.duration());
        existingActivity.setLocation(location);

        activityDao.updateActivity(existingActivity);
    }

}
