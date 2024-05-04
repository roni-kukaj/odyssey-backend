package com.odyssey.localCuisine;


import com.odyssey.locations.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class LocalCuisineJpaDataAccessServiceTest {
    private LocalCuisineJPADataAccessService localCuisineDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private LocalCuisineRepository localCuisineRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        localCuisineDataAccessService = new LocalCuisineJPADataAccessService(localCuisineRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllLocalCuisines() {
        localCuisineDataAccessService.selectAllLocalCuisines();
        verify(localCuisineRepository).findAll();
    }

    @Test
    void selectLocalCuisineById() {
        int id = 1;
        localCuisineDataAccessService.selectLocalCuisineById(id);
        verify(localCuisineRepository).findById(id);
    }

    @Test
    void selectLocalCuisinesByLocationId() {
        int locationId = 1;
        localCuisineDataAccessService.selectLocalCuisinesByLocationId(locationId);
        verify(localCuisineRepository).findByLocationId(locationId);
    }

    @Test
    void insertLocalCuisine() {
        LocalCuisine localCuisine = new LocalCuisine(1, "Name", "Smth...", "Pic.jpg", new Location());
        localCuisineDataAccessService.insertLocalCuisine(localCuisine);
        verify(localCuisineRepository).save(localCuisine);
    }

    @Test
    void updateLocalCuisine() {
        LocalCuisine localCuisine = new LocalCuisine(
                1, "Name", "Smth...", "pic.jpg", new Location()
        );
        localCuisineDataAccessService.updateLocalCuisine(localCuisine);
        verify(localCuisineRepository).save(localCuisine);
    }

    @Test
    void existsLocalCuisineById() {
        int id = 1;
        localCuisineDataAccessService.existsLocalCuisineById(id);
        verify(localCuisineRepository).existsLocalCuisineById(id);
    }

    @Test
    void existsLocalCuisineByNameAndLocationId() {
        String name = "nma";
        int locationId = 1;
        localCuisineDataAccessService.existsLocalCuisineByNameAndLocationId(name, locationId);
        verify(localCuisineRepository).existsLocalCuisineByNameAndLocationId(name, locationId);
    }

    @Test
    void deleteLocalCuisineById() {
        int id = 1;
        localCuisineDataAccessService.deleteLocalCuisineById(id);
        verify(localCuisineRepository).deleteById(id);
    }
}
