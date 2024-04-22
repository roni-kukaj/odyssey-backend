package com.odyssey.locations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    @Test
    public void testLocationGettersAndSetters() {
        Location location = new Location();
        location.setId(1);
        location.setCity("New York");
        location.setCountry("USA");
        location.setPicture("new_york.jpg");

        assertEquals(1, location.getId());
        assertEquals("New York", location.getCity());
        assertEquals("USA", location.getCountry());
        assertEquals("new_york.jpg", location.getPicture());
    }
}
