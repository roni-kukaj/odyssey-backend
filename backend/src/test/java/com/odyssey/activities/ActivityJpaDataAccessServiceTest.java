package com.odyssey.activities;

import com.odyssey.models.Location;
import com.odyssey.models.Activity;
import com.odyssey.repositories.ActivityRepository;
import com.odyssey.services.data.ActivityJPADataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class ActivityJpaDataAccessServiceTest {

    private ActivityJPADataAccessService activityDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        activityDataAccessService = new ActivityJPADataAccessService(activityRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllActivities() {
        activityDataAccessService.selectAllActivities();
        verify(activityRepository).findAll();
    }

    @Test
    void selectActivityById() {
        int id = 1;
        activityDataAccessService.selectActivityById(id);
        verify(activityRepository).findById(id);
    }

    @Test
    void selectActivitiesByLocationId() {
        int locationId = 1;
        activityDataAccessService.selectActivitiesByLocationId(locationId);
        verify(activityRepository).findByLocationId(locationId);
    }

    @Test
    void insertActivity() {
        Activity activity = new Activity(1, "Climbing", "...", 70, 15, new Location());
        activityDataAccessService.insertActivity(activity);
        verify(activityRepository).save(activity);
    }

    @Test
    void updateActivity() {
        Activity activity = new Activity(
                1, "aa", "aa", 1, 1, new Location()
        );
        activityDataAccessService.updateActivity(activity);
        verify(activityRepository).save(activity);
    }

    @Test
    void existsActivityById() {
        int id = 1;
        activityDataAccessService.existsActivityById(id);
        verify(activityRepository).existsActivityById(id);
    }

    @Test
    void existsActivityByNameAndLocationId() {
        String name = "a";
        int locationId = 1;
        activityDataAccessService.existsActivityByNameAndLocationId(name, locationId);
        verify(activityRepository).existsActivityByNameAndLocationId(name, locationId);
    }

    @Test
    void deleteActivityById() {
        int id = 1;
        activityDataAccessService.deleteActivityById(id);
        verify(activityRepository).deleteById(id);
    }
}