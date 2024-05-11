package com.odyssey.activities;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Activity> getActivitiesByLocationId(Integer locationId) {
        if (!locationDao.existsLocationById(locationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return activityDao.selectActivitiesByLocationId(locationId);
    }

    public Activity getActivity(Integer id) {
        return activityDao.selectActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("activity with id [%s] not found".formatted(id)));
    }

    public void addActivity(ActivityRegistrationRequest request) {
        if (activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("activity already exists");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        Activity activity = new Activity(
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

    public boolean updateActivity(Integer id, ActivityUpdateRequest request) {
        Activity existingActivity = getActivity(id);

        if (activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("activity already exists");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        boolean changes = false;

        if (request.name() != null && !request.name().equals(existingActivity.getName())) {
            existingActivity.setName(request.name());
            changes = true;
        }
        if (request.description() != null && !request.description().equals(existingActivity.getDescription())) {
            existingActivity.setDescription(request.description());
            changes = true;
        }
        if (request.cost() != null && !request.cost().equals(existingActivity.getCost())) {
            existingActivity.setCost(request.cost());
            changes = true;
        }
        if (request.duration() != null && !request.duration().equals(existingActivity.getDuration())) {
            existingActivity.setDuration(request.duration());
            changes = true;
        }
        if (request.locationId() != null && !request.locationId().equals(existingActivity.getLocation().getId())) {
            existingActivity.setLocation(location);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        activityDao.updateActivity(existingActivity);
        return changes;
    }

}
