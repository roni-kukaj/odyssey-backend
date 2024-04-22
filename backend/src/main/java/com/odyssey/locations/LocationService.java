package com.odyssey.locations;

import org.springframework.stereotype.Service;
import com.odyssey.exception.*;

import java.util.List;
import java.util.Optional;

@Service
public abstract class LocationService {

    private LocationDao locationDao;

    public LocationService() {
        this.locationDao = locationDao;
    }

    public List<Location> getAllLocations() {
        return locationDao.selectAllLocations();
    }

    public Location getLocation(Integer locationId) {
        return locationDao.selectLocationById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + locationId + " not found"));
    }


    public void addLocation(LocationRegistrationRequest locationRegistrationRequest) {
        Location location = new Location(
                locationRegistrationRequest.city(),
                locationRegistrationRequest.country(),
                locationRegistrationRequest.picture()
        );

        locationDao.insertLocation(location);
    }

    public abstract Optional<Location> getLocationById(Integer id);

    public abstract Location createLocation(Location location);

    public boolean deleteLocation(Integer id) {
        if (locationDao.existsLocationById(id)) {
            locationDao.deleteLocationById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Location with id [%s] not found".formatted(id));
        }
    }

    public boolean updateLocation(Integer id, LocationUpdateRequest updateRequest) {
        Location location = getLocation(id);
        boolean changes = false;

        if (updateRequest.city() != null && !updateRequest.city().equals(location.getCity())) {
            location.setCity(updateRequest.city());
            changes = true;
        }
        if (updateRequest.country() != null && !updateRequest.country().equals(location.getCountry())) {
            location.setCountry(updateRequest.country());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        locationDao.updateLocation(location);
        return changes;
    }

    public abstract boolean updateLocation(Location location);
}
