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
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationDao locationDao;
    @Mock
    private CloudinaryService cloudinaryService;

    private final String FILE_URL = "src/main/resources/images/test.png";
    private Path path;
    private byte[] content;

    private LocationService underTest;

    @BeforeEach
    void setUp() throws IOException {
        underTest = new LocationService(cloudinaryService, locationDao);
        path = Paths.get(FILE_URL);
        content = Files.readAllBytes(path);
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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocationRegistrationDto dto = new LocationRegistrationDto(
                city, country, image
        );

        // When
        underTest.addLocation(dto);

        // Then
        ArgumentCaptor<Location> locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationDao).insertLocation(locationArgumentCaptor.capture());

        Location capturedLocation = locationArgumentCaptor.getValue();

        assertThat(capturedLocation.getId()).isNull();
        assertThat(capturedLocation.getCity()).isEqualTo(dto.city());
        assertThat(capturedLocation.getCountry()).isEqualTo(dto.country());
        assertThat(capturedLocation.getPicture()).isEqualTo(null);

    }

    @Test
    void willThrowWhenCityAndCountryExistsWhileAddingLocation() {
        // Given
        String city = "Prishtina";
        String country = "Kosovo";
        when(locationDao.existsLocationByCityAndCountry(city, country)).thenReturn(true);

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocationRegistrationDto dto = new LocationRegistrationDto(
                city, country, image
        );

        // When
        assertThatThrownBy(() -> underTest.addLocation(dto))
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
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocationUpdateDto dto = new LocationUpdateDto(
                newCity, newCountry, image
        );

        lenient().when(locationDao.existsLocationByCityAndCountry(newCity, newCountry)).thenReturn(false);

        // When
        underTest.updateLocation(id, dto);

        // Then
        ArgumentCaptor<Location> locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationDao).updateLocation(locationArgumentCaptor.capture());

        Location capturedLocation = locationArgumentCaptor.getValue();

        assertThat(capturedLocation.getCity()).isEqualTo(dto.city());
        assertThat(capturedLocation.getCountry()).isEqualTo(dto.country());
        assertThat(capturedLocation.getPicture()).isEqualTo(null);
    }

}
