package com.odyssey.activities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("activityJPAService")
public interface ActivityRepository extends JpaRepository<Activity, Integer>{
    boolean existsActivityById(Integer id);
    boolean existsActivityByNameAndLocationId(String name, Integer locationId);

}
