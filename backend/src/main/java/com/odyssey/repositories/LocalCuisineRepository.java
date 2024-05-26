package com.odyssey.repositories;


import com.odyssey.models.LocalCuisine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalCuisineRepository extends JpaRepository<LocalCuisine, Integer> {
    boolean existsLocalCuisineById(Integer id);
    boolean existsLocalCuisineByNameAndLocationId(String name, Integer locationId);
    List<LocalCuisine> findByLocationId(Integer locationId);
}
