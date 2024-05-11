package com.odyssey.flights;

import java.sql.Timestamp;

public record FlightUpdateRequest(
        Timestamp departure, Integer originId, Integer destinationId
) {
}
