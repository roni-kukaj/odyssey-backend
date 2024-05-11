package com.odyssey.localCuisine;


import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class LocalCuisineService {

    private final LocalCuisineDao localCuisineDao;
    private final LocationDao locationDao;

    public LocalCuisineService(@Qualifier("localCuisineJPAService") LocalCuisineDao localCuisineDao, @Qualifier("locationJPAService") LocationDao locationDao) {
        this.localCuisineDao = localCuisineDao;
        this.locationDao = locationDao;
    }

    public List<LocalCuisine> getAllLocalCuisines() {
        return localCuisineDao.selectAllLocalCuisines();
    }

    public List<LocalCuisine> getLocalCuisinesByLocationId(Integer locationId) {
        if (!locationDao.existsLocationById(locationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return localCuisineDao.selectLocalCuisinesByLocationId(locationId);
    }

    public LocalCuisine getLocalCuisine(Integer id) {
        return localCuisineDao.selectLocalCuisineById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local Cuisine with id [%s] not found".formatted(id)));
    }

    public void addLocalCuisine(LocalCuisineRegistrationRequest request) {
        if (localCuisineDao.existsLocalCuisineByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("Local Cuisine already exists");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        LocalCuisine localCuisine = new LocalCuisine(
                null, // Assuming ID is auto-generated.
                request.name(),
                request.description(),
                request.image(),
                location
        );

        localCuisineDao.insertLocalCuisine(localCuisine);
    }

    public boolean deleteLocalCuisine(Integer id) {
        if (localCuisineDao.existsLocalCuisineById(id)) {
            localCuisineDao.deleteLocalCuisineById(id);
        } else {
            throw new ResourceNotFoundException("Local Cuisine with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean updateLocalCuisine(Integer id, LocalCuisineUpdateRequest request) {
        LocalCuisine existingLocalCuisine = getLocalCuisine(id);

        if (localCuisineDao.existsLocalCuisineByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("Local Cuisine already exists");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        boolean changes = false;

        if (request.name() != null && !request.name().equals(existingLocalCuisine.getName())) {
            existingLocalCuisine.setName(request.name());
            changes = true;
        }
        if (request.description() != null && !request.description().equals(existingLocalCuisine.getDescription())) {
            existingLocalCuisine.setDescription(request.description());
            changes = true;
        }
        if (request.image() != null && !request.image().equals(existingLocalCuisine.getImage())) {
            existingLocalCuisine.setImage(request.image());
            changes = true;
        }

        if (request.locationId() != null && !request.locationId().equals(existingLocalCuisine.getLocation().getId())) {
            existingLocalCuisine.setLocation(location);
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        localCuisineDao.updateLocalCuisine(existingLocalCuisine);
        return changes;
    }




}
