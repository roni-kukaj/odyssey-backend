package com.odyssey.locations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    boolean existsLocationById(Integer id);
    boolean existsByCityAndCountry(String city, String country);
}
