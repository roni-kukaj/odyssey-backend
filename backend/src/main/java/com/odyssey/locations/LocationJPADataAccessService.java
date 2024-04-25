package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LocationJPADataAccessService extends LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationJPADataAccessService(LocationRepository locationRepository) {
        super();
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> getLocationById(Integer id) {
        return locationRepository.findById(id);
    }

    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public boolean deleteLocation(Integer id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLocation(Location location) {
        if (locationRepository.existsById(location.getId())) {
            locationRepository.save(location);
            return true;
        }
        return false;
    }
}
