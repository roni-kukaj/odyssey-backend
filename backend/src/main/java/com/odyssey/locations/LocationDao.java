package com.odyssey.locations;

import java.util.List;
import java.util.Optional;

public interface LocationDao {
    List<Location> selectAllLocations();
    Optional<Location> selectLocationById(Integer id);
    void insertLocation(Location location);
    void updateLocation(Location location);
    boolean existsLocationById(Integer id);
    void deleteLocationById(Integer id);
}
