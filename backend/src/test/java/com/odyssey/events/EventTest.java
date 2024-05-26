package com.odyssey.events;

import com.odyssey.models.Location;
import com.odyssey.models.Event;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {


    @Test

    public void testEventGettersAndSetters(){
        Event event = new Event();
        event.setId(1);
        event.setName("Thanksgiving Day");
        event.setDescription("Experience the magic of Thanksgiving, an iconic NYC tradition!");

        event.setImage("thanksgiving_day.jpg");
        LocalDate date = LocalDate.of(2024, 11, 28);
        event.setDate(date);
        event.setCost(75.0);
        event.setDuration(2);
        Location location = new Location();
        location.setId(1);

        event.setLocation(location);


        assertEquals(1,event.getId());
        assertEquals("Thanksgiving Day",event.getName());
        assertEquals("Experience the magic of Thanksgiving, an iconic NYC tradition!",event.getDescription());
        assertEquals("thanksgiving_day.jpg",event.getImage());
        assertEquals(date,event.getDate());
        assertEquals(75.0,event.getCost());
        assertEquals(2,event.getDuration());
        assertEquals(location,event.getLocation());


    }
}
