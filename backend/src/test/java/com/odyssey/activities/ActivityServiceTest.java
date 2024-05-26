package com.odyssey.activities;

import com.odyssey.daos.ActivityDao;
import com.odyssey.dtos.ActivityRegistrationRequest;
import com.odyssey.dtos.ActivityUpdateRequest;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Activity;
import com.odyssey.services.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityDao activityDao;
    @Mock
    private LocationDao locationDao;

    private ActivityService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ActivityService(activityDao, locationDao);
    }

    @Test
    void getAllActivities() {
        // When
        underTest.getAllActivities();

        // Then
        verify(activityDao).selectAllActivities();
    }

    @Test
    void getActivity() {
        // Given
        int id = 1;
        Activity activity = new Activity(
                id,
                "A",
                "AA",
                75,
                3,
                new Location()
        );

        when(activityDao.selectActivityById(id)).thenReturn(Optional.of(activity));

        // When
        Activity actual = underTest.getActivity(id);

        // Then
        assertThat(actual).isEqualTo(activity);
    }

    @Test
    void willThrowWhenGetActivityReturnEmptyOptional() {
        // Given
        int id = 1;
        when(activityDao.selectActivityById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getActivity(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("activity with id [%s] not found".formatted(id));
    }

    @Test
    void willThrowWhenGetActivitiesByLocationIdNotFound() {
        // Given
        Location l = new Location();
        int locationId = 1;
        l.setId(locationId);
        when(locationDao.existsLocationById(locationId)).thenReturn(false);

        // When

        assertThatThrownBy(() -> underTest.getActivitiesByLocationId(locationId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        // Then
        verify(activityDao, never()).insertActivity(any());
    }

    @Test
    void addActivity() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "Hiking";

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, "HHH", 75, 1, locationId
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(activityDao.existsActivityByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        underTest.addActivity(request);

        // Then
        ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityDao).insertActivity(activityArgumentCaptor.capture());

        Activity capturedActivity = activityArgumentCaptor.getValue();

        assertThat(capturedActivity.getId()).isNull();
        assertThat(capturedActivity.getName()).isEqualTo(request.name());
        assertThat(capturedActivity.getDescription()).isEqualTo(request.description());
        assertThat(capturedActivity.getDuration()).isEqualTo(request.duration());
        assertThat(capturedActivity.getCost()).isEqualTo(request.cost());
        assertThat(capturedActivity.getLocation().getId()).isEqualTo(request.locationId());

    }

    @Test
    void willThrowAddActivityLocationNotExist() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "Hiking";

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, "HHH", 75, 1, locationId
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());
        when(activityDao.existsActivityByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.addActivity(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(request.locationId()));

        // Then
        verify(activityDao, never()).insertActivity(any());
    }

    @Test
    void willThrowAddActivityAlreadyExists() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "Hiking";

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, "HHH", 75, 1, locationId
        );
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(activityDao.existsActivityByNameAndLocationId(name, locationId)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addActivity(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("activity already exists");

        // Then
        verify(activityDao, never()).insertActivity(any());
    }

    @Test
    void deleteActivity() {
        // Given
        int id = 10;
        when(activityDao.existsActivityById(id)).thenReturn(true);

        // When
        underTest.deleteActivity(id);

        // Then
        verify(activityDao).deleteActivityById(id);
    }

    @Test
    void willThrowDeleteActivityNotExists() {
        // Given
        int id = 10;
        when(activityDao.existsActivityById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteActivity(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("activity with id [%s] not found".formatted(id));

        // Then
        verify(activityDao, never()).deleteActivityById(any());

    }

    @Test
    void updateActivity() {
        // Given
        int id = 10;
        Location l1 = new Location();
        l1.setId(1);
        Location l2 = new Location();
        l2.setId(2);
        Activity activity = new Activity(
                id,
                "A",
                "A",
                1,
                1,
                l1
        );

        when(activityDao.selectActivityById(id)).thenReturn(Optional.of(activity));

        String newName = "BBBBB";
        String newDesc = "BBBBB";
        int newCost = 2;
        int newDur = 2;

        ActivityUpdateRequest request = new ActivityUpdateRequest(
                newName,
                newDesc,
                newCost,
                newDur,
                l2.getId()
        );

        when(locationDao.selectLocationById(l2.getId())).thenReturn(Optional.of(l2));
        when(activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        underTest.updateActivity(id, request);

        // Then
        ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityDao).updateActivity(activityArgumentCaptor.capture());

        Activity capturedActivity = activityArgumentCaptor.getValue();

        assertThat(capturedActivity.getId()).isEqualTo(id);
        assertThat(capturedActivity.getName()).isEqualTo(request.name());
        assertThat(capturedActivity.getDescription()).isEqualTo(request.description());
        assertThat(capturedActivity.getDuration()).isEqualTo(request.duration());
        assertThat(capturedActivity.getCost()).isEqualTo(request.cost());
        assertThat(capturedActivity.getLocation().getId()).isEqualTo(request.locationId());

    }

    @Test
    void willThrowUpdateActivityNoDataChanges() {
        // Given
        int id = 10;
        Location l1 = new Location();
        l1.setId(1);
        Location l2 = new Location();
        l2.setId(2);
        Activity activity = new Activity(
                id,
                "A",
                "A",
                1,
                1,
                l1
        );

        when(activityDao.selectActivityById(id)).thenReturn(Optional.of(activity));

        ActivityUpdateRequest request = new ActivityUpdateRequest(
                activity.getName(),
                activity.getDescription(),
                activity.getCost(),
                activity.getDuration(),
                l1.getId()
        );

        when(locationDao.selectLocationById(l1.getId())).thenReturn(Optional.of(l1));
        when(activityDao.existsActivityByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.updateActivity(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes");

        // Then
        verify(activityDao, never()).updateActivity(any());
    }
}