package com.odyssey.hotels;

import java.util.List;
import java.util.Optional;

public interface HotelDao {
    List<Hotel> selectAllHotels();
    Optional<Hotel> selectHotelById(Integer id);
    List<Hotel> selectActivitiesByLocationId(Integer locationId);
    void insertHotel(Hotel hotel);
    void updateHotel(Hotel hotel);
    boolean existsHotelById(Integer id);
    boolean existsHotelByNameAndLocationId(String name, Integer locationId);
    void deleteHotelById(Integer id);
}
