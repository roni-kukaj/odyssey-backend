package com.odyssey.flights;

import java.sql.Timestamp;

public record FlightRegistrationRequest(
         Timestamp departure, Integer originId, Integer destinationId
) {
}
