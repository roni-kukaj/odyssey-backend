package com.odyssey.activities;

import com.odyssey.locations.Location;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void testActivityGettersAndSetters() {
        Activity activity = new Activity();
        Location l = new Location();
        activity.setId(1);
        activity.setName("A");
        activity.setDescription("aa");
        activity.setCost(75);
        activity.setLocation(l);
        activity.setDuration(15);

        assertEquals(1, activity.getId());
        assertEquals("A", activity.getName());
        assertEquals("aa", activity.getDescription());
        assertEquals(75, activity.getCost());
        assertEquals(l, activity.getLocation());
        assertEquals(15, activity.getDuration());


    }

}