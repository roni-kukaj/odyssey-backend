package com.odyssey.locations;

import com.odyssey.models.Location;
import com.odyssey.repositories.LocationRepository;
import com.odyssey.services.data.LocationJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

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
        locationDataAccessService.selectAllLocations();
        verify(locationRepository).findAll();
    }

    @Test
    void testGetLocationById() {
        int id = 1;
        locationDataAccessService.selectLocationById(id);
        verify(locationRepository).findById(id);
    }

    @Test
    void insertLocation() {
        Location location = new Location(1, "Prishtine", "Kosovo", "Picture 1");
        locationDataAccessService.insertLocation(location);
        verify(locationRepository).save(location);
    }

    @Test
    void existsLocationByCityAndCountry() {
        String city = "Prishtine", country = "Kosovo";
        locationDataAccessService.existsLocationByCityAndCountry(city, country);
        verify(locationRepository).existsByCityAndCountry(city, country);
    }

    @Test
    void existsLocationById() {
        int id = 1;
        locationDataAccessService.existsLocationById(id);
        verify(locationRepository).existsLocationById(id);
    }

    @Test
    void testDeleteLocation() {
        // Given
        int id = 1;
        // When
        locationDataAccessService.deleteLocationById(id);

        // Then
        verify(locationRepository).deleteById(id);
    }

    @Test
    void testUpdateLocation() {

    }


}
