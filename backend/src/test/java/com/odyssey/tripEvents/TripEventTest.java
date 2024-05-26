package com.odyssey.tripEvents;

import com.odyssey.models.Event;
import com.odyssey.models.TripEvent;
import com.odyssey.models.Trip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripEventTest {

    @Test
    void getTripEventsGettersAndSetters() {

        TripEvent tripEvent = new TripEvent();
        Trip trip = new Trip();
        Event event = new Event();
        tripEvent.setId(2);
        tripEvent.setEvent(event);
        tripEvent.setTrip(trip);

        assertEquals(2,tripEvent.getId());
        assertEquals(event,tripEvent.getEvent());
        assertEquals(trip,tripEvent.getTrip());

    }
}
