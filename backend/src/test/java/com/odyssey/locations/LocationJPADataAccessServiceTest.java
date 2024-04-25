package com.odyssey.locations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationJPADataAccessServiceTest {

    private LocationJPADataAccessService locationDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        locationDataAccessService = new LocationJPADataAccessService(locationRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetAllLocations() {
        // Given
        List<Location> locations = new ArrayList<>();
        when(locationRepository.findAll()).thenReturn(locations);

        // When
        locationDataAccessService.getAllLocations();

        // Then
        verify(locationRepository).findAll();
    }

    @Test
    void testGetLocationById() {
        // Given
        int id = 1;
        Location location = new Location();
        when(locationRepository.findById(id)).thenReturn(Optional.of(location));

        // When
        locationDataAccessService.getLocationById(id);

        // Then
        verify(locationRepository).findById(id);
    }

    @Test
    void testCreateLocation() {
        // Given
        Location location = new Location();

        // When
        locationDataAccessService.createLocation(location);

        // Then
        verify(locationRepository).save(location);
    }

    @Test
    void testDeleteLocation() {
        // Given
        int id = 1;
        when(locationRepository.existsById(id)).thenReturn(true);

        // When
        locationDataAccessService.deleteLocation(id);

        // Then
        verify(locationRepository).deleteById(id);
    }

    @Test
    void testUpdateLocation() {
        // Given
        Location location = new Location();
        location.setId(1); // Set id
        location.setCity("New York"); // Set city
        location.setCountry("USA"); // Set country

        // When
        when(locationRepository.existsById(any())).thenReturn(true); // Mock existsById method
        when(locationRepository.save(location)).thenReturn(location); // Mock save method
        boolean result = locationDataAccessService.updateLocation(location);

        // Then
        assertTrue(result); // Ensure update operation was successful
        verify(locationRepository).existsById(any());
        verify(locationRepository).save(location);
    }


}
