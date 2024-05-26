package com.odyssey.hotels;

import com.odyssey.models.Location;
import com.odyssey.models.Hotel;
import com.odyssey.repositories.HotelRepository;
import com.odyssey.services.data.HotelJPADataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class HotelJPADataAccessServiceTest {

    private HotelJPADataAccessService hotelDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        hotelDataAccessService = new HotelJPADataAccessService(hotelRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllHotels() {
        hotelDataAccessService.selectAllHotels();
        verify(hotelRepository).findAll();
    }

    @Test
    void selectHotelById() {
        int id = 1;
        hotelDataAccessService.selectHotelById(id);
        verify(hotelRepository).findById(id);
    }

    @Test
    void selectActivitiesByLocationId() {
        int locationId = 1;
        hotelDataAccessService.selectActivitiesByLocationId(locationId);
        verify(hotelRepository).findByLocationId(locationId);
    }

    @Test
    void insertHotel() {
        Hotel hotel = new Hotel(1, "Hotel A", new Location(), 4.6, "link");
        hotelDataAccessService.insertHotel(hotel);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void updateHotel() {
        Hotel hotel = new Hotel(1, "Hotel A", new Location(), 4.6, "link");
        hotelDataAccessService.updateHotel(hotel);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void existsHotelById() {
        int id = 1;
        hotelDataAccessService.existsHotelById(id);
        verify(hotelRepository).existsHotelById(id);
    }

    @Test
    void existsHotelByNameAndLocationId() {
        String name = "";
        int locationId = 1;
        hotelDataAccessService.existsHotelByNameAndLocationId(name, locationId);
        verify(hotelRepository).existsHotelByNameAndLocationId(name, locationId);
    }

    @Test
    void deleteHotelById() {
        int id = 1;
        hotelDataAccessService.deleteHotelById(id);
        verify(hotelRepository).deleteById(id);
    }
}