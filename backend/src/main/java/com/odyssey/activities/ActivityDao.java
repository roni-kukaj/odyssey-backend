package com.odyssey.activities;

import com.odyssey.user.User;

import java.util.List;
import java.util.Optional;

public interface ActivityDao {

    List<Activity> selectAllActivities();
    Optional<Activity> selectActivityById(Integer Id);
    void insertActivity(Activity activity);
    void updateActivity(Activity activity);
    boolean existsActivityById(Integer Id);
    boolean existsActivityByNameAndLocationId(String name, Integer locationId);
    void deleteActivityById(Integer id);

}
