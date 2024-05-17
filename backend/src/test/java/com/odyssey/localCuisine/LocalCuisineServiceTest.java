package com.odyssey.localCuisine;



import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalCuisineServiceTest {

    @Mock
    private LocalCuisineDao localCuisineDao;
    @Mock
    private LocationDao locationDao;
    @Mock
    private CloudinaryService cloudinaryService;

    private LocalCuisineService underTest;

    @BeforeEach
    void setUp() {
        underTest = new LocalCuisineService(localCuisineDao, locationDao, cloudinaryService);
    }


    @Test
    void getAllLocalCuisines() {
        // When
        underTest.getAllLocalCuisines();

        // Then
        verify(localCuisineDao).selectAllLocalCuisines();
    }

    @Test
    void getLocalCuisine() {
        // Given
        int id = 1;
        LocalCuisine localCuisine = new LocalCuisine(
                id,
                "Emrii",
                "Desc",
                "foto.png",
                new Location()
        );

        when(localCuisineDao.selectLocalCuisineById(id)).thenReturn(Optional.of(localCuisine));

        // When
        LocalCuisine actual = underTest.getLocalCuisine(id);

        // Then
        assertThat(actual).isEqualTo(localCuisine);
    }

    @Test
    void willThrowWhenGetLocalCuisineReturnEmptyOptional() {
        // Given
        int id = 1;
        when(localCuisineDao.selectLocalCuisineById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getLocalCuisine(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Local Cuisine with id [%s] not found".formatted(id));
    }

    @Test
    void getLocalCuisinesByLocationId() {
        // Given
        Location l = new Location();
        int locationId = 1;
        l.setId(locationId);
        when(locationDao.existsLocationById(locationId)).thenReturn(false);

        // When

        assertThatThrownBy(() -> underTest.getLocalCuisinesByLocationId(locationId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        // Then
        verify(localCuisineDao, never()).insertLocalCuisine(any());
    }

    @Test
    void addLocalCuisine() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "YourName";

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, "Description",  "pic.jpg", locationId
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        underTest.addLocalCuisine(request);

        // Then
        ArgumentCaptor<LocalCuisine> localCuisineArgumentCaptor = ArgumentCaptor.forClass(LocalCuisine.class);
        verify(localCuisineDao).insertLocalCuisine(localCuisineArgumentCaptor.capture());

        LocalCuisine capturedLocalCuisine = localCuisineArgumentCaptor.getValue();

        assertThat(capturedLocalCuisine.getId()).isNull();
        assertThat(capturedLocalCuisine.getName()).isEqualTo(request.name());
        assertThat(capturedLocalCuisine.getDescription()).isEqualTo(request.description());
        assertThat(capturedLocalCuisine.getImage()).isEqualTo(request.image());
        assertThat(capturedLocalCuisine.getLocation().getId()).isEqualTo(request.locationId());

    }

    @Test
    void willThrowAddLocalCuisineLocationNotExist() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "YourName";

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, "Description", "image.jpg", locationId
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.addLocalCuisine(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(request.locationId()));

        // Then
        verify(localCuisineDao, never()).insertLocalCuisine(any());
    }

    @Test
    void willThrowAddLocalCuisineAlreadyExists() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "YourName";

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, "Description", "pic.jpg",  locationId
        );
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addLocalCuisine(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Local Cuisine already exists");

        // Then
        verify(localCuisineDao, never()).insertLocalCuisine(any());
    }

    @Test
    void deleteLocalCuisine() {
        // Given
        int id = 10;
        when(localCuisineDao.existsLocalCuisineById(id)).thenReturn(true);

        // When
        underTest.deleteLocalCuisine(id);

        // Then
        verify(localCuisineDao).deleteLocalCuisineById(id);
    }

    @Test
    void willThrowDeleteLocalCuisineNotExists() {
        // Given
        int id = 10;
        when(localCuisineDao.existsLocalCuisineById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteLocalCuisine(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Local Cuisine with id [%s] not found".formatted(id));

        // Then
        verify(localCuisineDao, never()).deleteLocalCuisineById(any());

    }

    @Test
    void updateLocalCuisine() {
        // Given
        int id = 10;
        Location l1 = new Location();
        l1.setId(1);
        Location l2 = new Location();
        l2.setId(2);
        LocalCuisine localCuisine = new LocalCuisine(
                id,
                "YourName",
                "Description",
                "pic.jpg",
                l1
        );

        when(localCuisineDao.selectLocalCuisineById(id)).thenReturn(Optional.of(localCuisine));

        String newName = "Name";
        String newDesc = "Desc";
        String newImage = "image.jpg";


        LocalCuisineUpdateRequest request = new LocalCuisineUpdateRequest(
                newName,
                newDesc,
                newImage,
                l2.getId()
        );

        when(locationDao.selectLocationById(l2.getId())).thenReturn(Optional.of(l2));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        underTest.updateLocalCuisine(id, request);

        // Then
        ArgumentCaptor<LocalCuisine> localCuisineArgumentCaptor = ArgumentCaptor.forClass(LocalCuisine.class);
        verify(localCuisineDao).updateLocalCuisine(localCuisineArgumentCaptor.capture());

        LocalCuisine capturedLocalCuisine = localCuisineArgumentCaptor.getValue();

        assertThat(capturedLocalCuisine.getId()).isEqualTo(id);
        assertThat(capturedLocalCuisine.getName()).isEqualTo(request.name());
        assertThat(capturedLocalCuisine.getDescription()).isEqualTo(request.description());
        assertThat(capturedLocalCuisine.getImage()).isEqualTo(request.image());
        assertThat(capturedLocalCuisine.getLocation().getId()).isEqualTo(request.locationId());

    }

    @Test
    void willThrowUpdateLocalCuisineNoDataChanges() {
        // Given
        int id = 10;
        Location l1 = new Location();
        l1.setId(1);
        Location l2 = new Location();
        l2.setId(2);
        LocalCuisine localCuisine = new LocalCuisine(
                id,
                "Emri",
                "Descr",
                "foto.png",
                l1
        );

        when(localCuisineDao.selectLocalCuisineById(id)).thenReturn(Optional.of(localCuisine));

        LocalCuisineUpdateRequest request = new LocalCuisineUpdateRequest(
                localCuisine.getName(),
                localCuisine.getDescription(),
                localCuisine.getImage(),
                l1.getId()
        );

        when(locationDao.selectLocationById(l1.getId())).thenReturn(Optional.of(l1));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.updateLocalCuisine(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes");

        // Then
        verify(localCuisineDao, never()).updateLocalCuisine(any());
    }


}
