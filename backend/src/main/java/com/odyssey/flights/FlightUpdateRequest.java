package com.odyssey.flights;

import java.sql.Timestamp;

public record FlightUpdateRequest(
        String name, Timestamp time, Integer origin, Integer destination
) {
}
