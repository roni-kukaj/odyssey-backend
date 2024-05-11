package com.odyssey.tripItems;

import com.odyssey.items.Item;
import com.odyssey.trips.Trip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripItemTest {

    @Test
    void testTripItemGettersAndSetters() {

        TripItem tripItem = new TripItem();
        Trip trip = new Trip();
        Item item = new Item();

        tripItem.setId(2);
        tripItem.setItem(item);
        tripItem.setTrip(trip);

        assertEquals(2,tripItem.getId());
        assertEquals(item,tripItem.getItem());
        assertEquals(trip,tripItem.getTrip());
    }
}
