package com.odyssey.locations;

import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    @Mock
    private LocationDao locationDao;

    @InjectMocks
    private LocationService locationService = new LocationService() {
        @Override
        public Optional<Location> getLocationById(Integer id) {
            return Optional.empty();
        }

        @Override
        public Location createLocation(Location location) {
            return null;
        }

        @Override
        public boolean updateLocation(Location location) {
            return false;
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLocations() {
        // Test getAllLocations method
        assertNotNull(locationService.getAllLocations());
    }

    @Test
    void testGetLocationById() {
        // Test getLocationById method
        assertThrows(ResourceNotFoundException.class, () -> locationService.getLocation(1));
    }

    @Test
    void testAddLocation() {
        // Test addLocation method
        LocationRegistrationRequest registrationRequest = new LocationRegistrationRequest(null, "City", "Country", "Picture");
        assertDoesNotThrow(() -> locationService.addLocation(registrationRequest));
    }

    @Test
    void testDeleteLocation() {
        // Test deleteLocation method
        assertThrows(ResourceNotFoundException.class, () -> locationService.deleteLocation(1));
    }

    @Test
    void testUpdateLocation() {
        // Arrange
        Location location = new Location();
        LocationUpdateRequest updateRequest = new LocationUpdateRequest(null, "New York", "USA");

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> locationService.updateLocation(location.getId(), updateRequest));
    }

}
