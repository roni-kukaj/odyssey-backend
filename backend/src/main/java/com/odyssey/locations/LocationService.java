package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.exception.*;

import java.util.List;

@Service
public class LocationService {

    private final LocationDao locationDao;

    public LocationService(@Qualifier("locationJPAService") LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public List<Location> getAllLocations() {
        return locationDao.selectAllLocations();
    }

    public Location getLocation(Integer id) {
        return locationDao.selectLocationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(id)));
    }


    public void addLocation(LocationRegistrationRequest locationRegistrationRequest) {
        if (locationDao.existsLocationByCityAndCountry(locationRegistrationRequest.city(), locationRegistrationRequest.country())) {
            throw new DuplicateResourceException("location already exists");
        }
        Location location = new Location(
                locationRegistrationRequest.city(),
                locationRegistrationRequest.country(),
                locationRegistrationRequest.picture()
        );

        locationDao.insertLocation(location);
    }

    public boolean deleteLocation(Integer id) {
        if (locationDao.existsLocationById(id)) {
            locationDao.deleteLocationById(id);
        } else {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(id));
        }
        return false;
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
        if (updateRequest.picture() != null && !updateRequest.picture().equals(location.getPicture())) {
            location.setPicture(updateRequest.picture());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        locationDao.updateLocation(location);
        return changes;
    }

}
