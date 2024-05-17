package com.odyssey.locations;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationDao locationDao;
    @Mock
    private CloudinaryService cloudinaryService;

    private LocationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new LocationService(cloudinaryService, locationDao);
    }

    @Test
    void testGetAllLocations() {
        // When
        underTest.getAllLocations();

        // Then
        verify(locationDao).selectAllLocations();
    }

    @Test
    void testGetLocationById() {
        // Given
        int id = 1;
        Location location = new Location(
                id,
                "Prishtine",
                "Kosovo",
                "Picture 1"
        );

        when(locationDao.selectLocationById(id)).thenReturn(Optional.of(location));

        // When
        Location actual = underTest.getLocation(id);

        // Then
        assertThat(actual).isEqualTo(location);
    }

    @Test
    void willThrowWhenGetLocationReturnEmptyOptional() {
        // Given
        int id = 1;
        when(locationDao.selectLocationById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getLocation(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(id));

    }

    @Test
    void testAddLocation() {
        // Given
        String city = "Prishtina";
        String country = "Kosovo";
        when(locationDao.existsLocationByCityAndCountry(city, country)).thenReturn(false);

        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city,
                country,
                "Picture 1"
        );

        // When
        underTest.addLocation(request);

        // Then
        ArgumentCaptor<Location> locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationDao).insertLocation(locationArgumentCaptor.capture());

        Location capturedLocation = locationArgumentCaptor.getValue();

        assertThat(capturedLocation.getId()).isNull();
        assertThat(capturedLocation.getCity()).isEqualTo(request.city());
        assertThat(capturedLocation.getCountry()).isEqualTo(request.country());
        assertThat(capturedLocation.getPicture()).isEqualTo(request.picture());

    }

    @Test
    void willThrowWhenCityAndCountryExistsWhileAddingLocation() {
        // Given
        String city = "Prishtina";
        String country = "Kosovo";
        when(locationDao.existsLocationByCityAndCountry(city, country)).thenReturn(true);

        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city,
                country,
                "Picture 1"
        );

        // When
        assertThatThrownBy(() -> underTest.addLocation(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("location already exists");
    }


    @Test
    void deleteLocationById() {
        // Given
        int id = 10;
        when(locationDao.existsLocationById(id)).thenReturn(true);

        // When
        underTest.deleteLocation(id);

        // Then
        verify(locationDao).deleteLocationById(id);
    }

    @Test
    void willThrowDeleteLocationByIdNotExists() {
        // Given
        int id = 10;

        when(locationDao.existsLocationById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteLocation(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(id));

        // Then
        verify(locationDao, never()).deleteLocationById(any());
    }

    @Test
    void canUpdateAllLocationProperties() {
        // Given
        int id = 10;
        Location location = new Location(
                id,
                "Prishtine",
                "Kosovo",
                "Picture 1"
        );

        when(locationDao.selectLocationById(id)).thenReturn(Optional.of(location));

        String newCity = "New York";
        String newCountry = "USA";
        LocationUpdateRequest request = new LocationUpdateRequest(
                newCity,
                newCountry,
                "Picture 2"
        );
        lenient().when(locationDao.existsLocationByCityAndCountry(newCity, newCountry)).thenReturn(false);

        // When
        underTest.updateLocation(id, request);

        // Then
        ArgumentCaptor<Location> locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationDao).updateLocation(locationArgumentCaptor.capture());

        Location capturedLocation = locationArgumentCaptor.getValue();

        assertThat(capturedLocation.getCity()).isEqualTo(request.city());
        assertThat(capturedLocation.getCountry()).isEqualTo(request.country());
        assertThat(capturedLocation.getPicture()).isEqualTo(request.picture());
    }

}
