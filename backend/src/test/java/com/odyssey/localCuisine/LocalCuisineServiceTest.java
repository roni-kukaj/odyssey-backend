package com.odyssey.localCuisine;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.LocalCuisineDao;
import com.odyssey.dtos.LocalCuisineRegistrationDto;
import com.odyssey.dtos.LocalCuisineUpdateDto;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.LocalCuisine;
import com.odyssey.services.LocalCuisineService;
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
public class LocalCuisineServiceTest {

    @Mock
    private LocalCuisineDao localCuisineDao;
    @Mock
    private LocationDao locationDao;
    @Mock
    private CloudinaryService cloudinaryService;

    private final String FILE_URL = "src/main/resources/images/test.png";
    private Path path;
    private byte[] content;

    private LocalCuisineService underTest;

    @BeforeEach
    void setUp() throws IOException {
        underTest = new LocalCuisineService(localCuisineDao, locationDao, cloudinaryService);
        path = Paths.get(FILE_URL);
        content = Files.readAllBytes(path);
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
                .hasMessage("local cuisine with id [%s] not found".formatted(id));
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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

       LocalCuisineRegistrationDto dto = new LocalCuisineRegistrationDto(
               name, "", image, locationId
       );

        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        underTest.addLocalCuisine(dto);

        // Then
        ArgumentCaptor<LocalCuisine> localCuisineArgumentCaptor = ArgumentCaptor.forClass(LocalCuisine.class);
        verify(localCuisineDao).insertLocalCuisine(localCuisineArgumentCaptor.capture());

        LocalCuisine capturedLocalCuisine = localCuisineArgumentCaptor.getValue();

        assertThat(capturedLocalCuisine.getId()).isNull();
        assertThat(capturedLocalCuisine.getName()).isEqualTo(dto.name());
        assertThat(capturedLocalCuisine.getDescription()).isEqualTo(dto.description());
        assertThat(capturedLocalCuisine.getImage()).isEqualTo(null);
        assertThat(capturedLocalCuisine.getLocation().getId()).isEqualTo(dto.locationId());

    }

    @Test
    void willThrowAddLocalCuisineLocationNotExist() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "YourName";

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocalCuisineRegistrationDto dto = new LocalCuisineRegistrationDto(
                name, "", image, locationId
        );

        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.addLocalCuisine(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(dto.locationId()));

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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocalCuisineRegistrationDto dto = new LocalCuisineRegistrationDto(
                name, "", image, locationId
        );
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(localCuisineDao.existsLocalCuisineByNameAndLocationId(name, locationId)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addLocalCuisine(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("local cuisine already exists");

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
                .hasMessage("local cuisine with id [%s] not found".formatted(id));

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
                null,
                l1
        );

        when(localCuisineDao.selectLocalCuisineById(id)).thenReturn(Optional.of(localCuisine));

        String newName = "Name";
        String newDesc = "Desc";

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        LocalCuisineUpdateDto dto = new LocalCuisineUpdateDto(
                newName, newDesc, l2.getId(), image
        );

        when(locationDao.selectLocationById(l2.getId())).thenReturn(Optional.of(l2));

        // When
        underTest.updateLocalCuisine(id, dto);

        // Then
        ArgumentCaptor<LocalCuisine> localCuisineArgumentCaptor = ArgumentCaptor.forClass(LocalCuisine.class);
        verify(localCuisineDao).updateLocalCuisine(localCuisineArgumentCaptor.capture());

        LocalCuisine capturedLocalCuisine = localCuisineArgumentCaptor.getValue();

        assertThat(capturedLocalCuisine.getId()).isEqualTo(id);
        assertThat(capturedLocalCuisine.getName()).isEqualTo(dto.name());
        assertThat(capturedLocalCuisine.getDescription()).isEqualTo(dto.description());
        assertThat(capturedLocalCuisine.getImage()).isEqualTo(null);
        assertThat(capturedLocalCuisine.getLocation().getId()).isEqualTo(dto.locationId());

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

        LocalCuisineUpdateDto dto = new LocalCuisineUpdateDto(
                localCuisine.getName(),
                localCuisine.getDescription(),
                l1.getId(),
                null
        );

        when(locationDao.selectLocationById(l1.getId())).thenReturn(Optional.of(l1));

        // When
        assertThatThrownBy(() -> underTest.updateLocalCuisine(id, dto))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes");

        // Then
        verify(localCuisineDao, never()).updateLocalCuisine(any());
    }


}
