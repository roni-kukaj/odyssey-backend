package com.odyssey.tripActivities;

import com.odyssey.models.Activity;
import com.odyssey.models.TripActivity;
import com.odyssey.models.Trip;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;





public class TripActivityTest {

    @Test
    void testTripActivityGettersAndSetters() {
        TripActivity tripActivity = new TripActivity();
        Trip trip = new Trip();
        Activity activity = new Activity();
        tripActivity.setId(2);
        tripActivity.setActivity(activity);
        tripActivity.setTrip(trip);

        assertEquals(2,tripActivity.getId());
        assertEquals(activity,tripActivity.getActivity());
        assertEquals(trip,tripActivity.getTrip());








    }
}
