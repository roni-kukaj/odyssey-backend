package com.odyssey.hotels;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelDao hotelDao;
    private final LocationDao locationDao;

    public HotelService(
            @Qualifier("hotelJPAService") HotelDao hotelDao,
            @Qualifier("locationJPAService") LocationDao locationDao
    ) {
        this.hotelDao = hotelDao;
        this.locationDao = locationDao;
    }

    public List<Hotel> getAllHotels() {
        return hotelDao.selectAllHotels();
    }

    public Hotel getHotel(Integer id) {
        return hotelDao.selectHotelById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel with id [%s] not found".formatted(id)));
    }

    public List<Hotel> getHotelsByLocationId(Integer locationId) {
        if (!locationDao.existsLocationById(locationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return hotelDao.selectActivitiesByLocationId(locationId);
    }

    public void addHotel(HotelRegistrationRequest request) {
        if (hotelDao.existsHotelByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("hotel already exists");
        }
        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        Hotel hotel = new Hotel(
                request.name(),
                location,
                request.rating(),
                request.bookingLink()
        );

        hotelDao.insertHotel(hotel);
    }

    public void deleteHotel(Integer id) {
        if (hotelDao.existsHotelById(id)) {
            hotelDao.deleteHotelById(id);
        }
        else {
            throw new ResourceNotFoundException("hotel with id [%s] not found".formatted(id));
        }
    }

    public void updateHotel(Integer id, HotelUpdateRequest request) {
        Hotel existingHotel = getHotel(id);

        if (hotelDao.existsHotelByNameAndLocationId(request.name(), request.locationId())) {
            throw new DuplicateResourceException("hotel already exists");
        }

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        boolean changes = false;

        if (request.name() != null && !request.name().equals(existingHotel.getName())) {
            existingHotel.setName(request.name());
            changes = true;
        }
        if (request.locationId() != null && !request.locationId().equals(existingHotel.getLocation().getId())) {
            existingHotel.setLocation(location);
            changes = true;
        }
        if (request.rating() != null && !request.rating().equals(existingHotel.getRating())) {
            existingHotel.setRating(request.rating());
            changes = true;
        }
        if (request.bookingLink() != null && !request.bookingLink().equals(existingHotel.getBookingLink())) {
            existingHotel.setBookingLink(request.bookingLink());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        hotelDao.updateHotel(existingHotel);
    }

}
