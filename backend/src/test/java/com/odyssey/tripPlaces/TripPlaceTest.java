package com.odyssey.tripPlaces;

import com.odyssey.locations.Location;
import com.odyssey.trips.Trip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripPlaceTest {

    @Test
    void testTripPlaceGettersAndSetters() {

        TripPlace tripPlace = new TripPlace();
        Trip trip = new Trip();
        Location place = new Location();

        tripPlace.setId(2);
        tripPlace.setTrip(trip);
        tripPlace.setLocation(place);

        assertEquals(2,tripPlace.getId());
        assertEquals(trip,tripPlace.getTrip());
        assertEquals(place,tripPlace.getLocation());
    }
}
