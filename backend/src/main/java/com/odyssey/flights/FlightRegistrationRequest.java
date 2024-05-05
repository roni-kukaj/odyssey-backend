package com.odyssey.flights;

import java.sql.Timestamp;

public record FlightRegistrationRequest(
        String name, Timestamp time, Integer origin, Integer destination
) {
}
