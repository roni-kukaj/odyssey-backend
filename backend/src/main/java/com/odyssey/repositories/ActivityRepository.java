package com.odyssey.repositories;

import com.odyssey.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{
    boolean existsActivityById(Integer id);
    boolean existsActivityByNameAndLocationId(String name, Integer locationId);
    List<Activity> findByLocationId(Integer locationId);
}
