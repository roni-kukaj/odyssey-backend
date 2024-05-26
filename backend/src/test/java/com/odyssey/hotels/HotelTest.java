package com.odyssey.hotels;

import com.odyssey.models.Location;
import com.odyssey.models.Hotel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HotelTest {

    @Test
    void testHotelGettersAndSetters() {
        Hotel hotel = new Hotel();
        Location l = new Location();
        hotel.setId(1);
        hotel.setName("Hotel A");
        hotel.setLocation(l);
        hotel.setRating(4.7);
        hotel.setBookingLink("link to booking...");

        assertEquals(1, hotel.getId());
        assertEquals("Hotel A", hotel.getName());
        assertEquals(l, hotel.getLocation());
        assertEquals(4.7, hotel.getRating());
        assertEquals("link to booking...", hotel.getBookingLink());
    }

}