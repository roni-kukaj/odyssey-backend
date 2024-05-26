package com.odyssey.services.data;

import com.odyssey.daos.HotelDao;
import com.odyssey.repositories.HotelRepository;
import com.odyssey.models.Hotel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("hotelJPAService")
public class HotelJPADataAccessService implements HotelDao {

    private final HotelRepository hotelRepository;

    public HotelJPADataAccessService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> selectAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Optional<Hotel> selectHotelById(Integer id) {
        return hotelRepository.findById(id);
    }

    @Override
    public List<Hotel> selectActivitiesByLocationId(Integer locationId) {
        return hotelRepository.findByLocationId(locationId);
    }

    @Override
    public void insertHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public boolean existsHotelById(Integer id) {
        return hotelRepository.existsHotelById(id);
    }

    @Override
    public boolean existsHotelByNameAndLocationId(String name, Integer locationId) {
        return hotelRepository.existsHotelByNameAndLocationId(name, locationId);
    }

    @Override
    public void deleteHotelById(Integer id) {
        hotelRepository.deleteById(id);
    }
}
