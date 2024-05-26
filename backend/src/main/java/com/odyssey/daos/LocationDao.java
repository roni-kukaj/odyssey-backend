package com.odyssey.daos;

import com.odyssey.models.Location;

import java.util.List;
import java.util.Optional;

public interface LocationDao {
    List<Location> selectAllLocations();
    Optional<Location> selectLocationById(Integer id);
    void insertLocation(Location location);
    void updateLocation(Location location);
    boolean existsLocationById(Integer id);
    boolean existsLocationByCityAndCountry(String city, String country);
    void deleteLocationById(Integer id);
}
