package com.odyssey.hotels;

import com.odyssey.daos.HotelDao;
import com.odyssey.dtos.HotelRegistrationRequest;
import com.odyssey.dtos.HotelUpdateRequest;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Hotel;
import com.odyssey.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class HotelServiceTest {

    @Mock
    private HotelDao hotelDao;
    @Mock
    private LocationDao locationDao;

    private HotelService underTest;

    @BeforeEach
    void setUp() {
        underTest = new HotelService(hotelDao, locationDao);
    }

    @Test
    void getAllHotels() {
        // When
        underTest.getAllHotels();

        // Then
        verify(hotelDao).selectAllHotels();
    }

    @Test
    void getHotel() {
        // Given
        int id = 1;
        Hotel hotel = new Hotel(1, "Hotel A", new Location(), 4.6, "link");
        when(hotelDao.selectHotelById(id)).thenReturn(Optional.of(hotel));

        // When
        Hotel actual = underTest.getHotel(id);

        // Then
        assertThat(actual).isEqualTo(hotel);
    }

    @Test
    void willThrowWhenGetHotelReturnEmptyOptional() {
        // Given
        int id = 1;
        when(hotelDao.selectHotelById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getHotel(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("hotel with id [%s] not found".formatted(id));
    }

    @Test
    void addHotel() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "Continental";

        HotelRegistrationRequest request = new HotelRegistrationRequest(
                name, locationId, 4.2, "link"
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(hotelDao.existsHotelByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        underTest.addHotel(request);

        // Then
        ArgumentCaptor<Hotel> activityArgumentCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelDao).insertHotel(activityArgumentCaptor.capture());

        Hotel capturedHotel = activityArgumentCaptor.getValue();

        assertThat(capturedHotel.getId()).isNull();
        assertThat(capturedHotel.getName()).isEqualTo(request.name());
        assertThat(capturedHotel.getLocation().getId()).isEqualTo(request.locationId());
        assertThat(capturedHotel.getRating()).isEqualTo(request.rating());
        assertThat(capturedHotel.getBookingLink()).isEqualTo(request.bookingLink());
    }

    @Test
    void willThrowWhenAddHotelLocationNotExist() {
        // Given
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        String name = "Continental";

        HotelRegistrationRequest request = new HotelRegistrationRequest(
                name, locationId, 4.2, "link"
        );
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());
        when(hotelDao.existsHotelByNameAndLocationId(name, locationId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.addHotel(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(request.locationId()));

        // Then
        verify(hotelDao, never()).insertHotel(any());
    }

    @Test
    void deleteHotel() {
        // Given
        int id = 10;
        when(hotelDao.existsHotelById(id)).thenReturn(true);

        // When
        underTest.deleteHotel(id);

        // Then
        verify(hotelDao).deleteHotelById(id);
    }

    @Test
    void willThrowDeleteHotelNotExists() {
        // Given
        int id = 10;
        when(hotelDao.existsHotelById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteHotel(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("hotel with id [%s] not found".formatted(id));

        // Then
        verify(hotelDao, never()).deleteHotelById(any());

    }

    @Test
    void updateHotel() {
        // Given
        int id = 10;
        Location location = new Location();
        location.setId(1);
        Hotel hotel = new Hotel(
                "A", location, 4.1, "link1"
        );
        when(hotelDao.selectHotelById(id)).thenReturn(Optional.of(hotel));

        String newName = "B";
        Location newLocation = new Location();
        newLocation.setId(2);
        double newRating = 1.1;
        String newLink = "new link";

        HotelUpdateRequest request = new HotelUpdateRequest(
                newName, newLocation.getId(), newRating, newLink
        );
        when(locationDao.selectLocationById(newLocation.getId())).thenReturn(Optional.of(newLocation));
        when(hotelDao.existsHotelByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        underTest.updateHotel(id, request);

        // Then
        ArgumentCaptor<Hotel> activityArgumentCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelDao).updateHotel(activityArgumentCaptor.capture());

        Hotel capturedHotel = activityArgumentCaptor.getValue();

        assertThat(capturedHotel.getId()).isNull();
        assertThat(capturedHotel.getName()).isEqualTo(request.name());
        assertThat(capturedHotel.getLocation().getId()).isEqualTo(request.locationId());
        assertThat(capturedHotel.getRating()).isEqualTo(request.rating());
        assertThat(capturedHotel.getBookingLink()).isEqualTo(request.bookingLink());
    }

    @Test
    void willThrowUpdateHotelNoDataChanges() {
        // Given
        int id = 10;
        Location location = new Location();
        location.setId(1);
        Hotel hotel = new Hotel(
                "A", location, 4.1, "link1"
        );
        when(hotelDao.selectHotelById(id)).thenReturn(Optional.of(hotel));

        HotelUpdateRequest request = new HotelUpdateRequest(
                hotel.getName(), hotel.getLocation().getId(), hotel.getRating(), hotel.getBookingLink()
        );
        when(locationDao.selectLocationById(location.getId())).thenReturn(Optional.of(location));
        when(hotelDao.existsHotelByNameAndLocationId(request.name(), request.locationId())).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.updateHotel(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes");

        // Then
        verify(hotelDao, never()).updateHotel(any());
    }

}