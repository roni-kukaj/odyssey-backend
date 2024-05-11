package com.odyssey.trips;

import com.odyssey.activities.Activity;
import com.odyssey.events.Event;
import com.odyssey.items.Item;
import com.odyssey.locations.Location;
import com.odyssey.user.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripTest {

    @Test
    void testTripGettersAndSetters() {
        Trip trip = new Trip();
        User user = new User();
        Set<Item> items = new HashSet<>();
        Set<Location>location = new HashSet<>();
        Set<Activity>activities = new HashSet<>();
        Set<Event>events = new HashSet<>();

        trip.setId(1);
        trip.setUser(user);
        LocalDate startDate = LocalDate.of(2024,6,25);
        LocalDate endDate = LocalDate.of(2024,7,3);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setActivities(activities);
        trip.setEvents(events);
        trip.setPlaces(location);
        trip.setItems(items);


        assertEquals(1,trip.getId());
        assertEquals(startDate,trip.getStartDate());
        assertEquals(endDate,trip.getEndDate());
        assertEquals(items,trip.getItems());
        assertEquals(location,trip.getPlaces());
        assertEquals(activities,trip.getActivities());
        assertEquals(events,trip.getEvents());



    }
}
