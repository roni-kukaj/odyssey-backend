package com.odyssey.flights;

import com.odyssey.locations.Location;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightTest {
    @Test
    void testFlightGettersAndSetters() {
        Location origin = new Location();
        Location destination = new Location();
        LocalDateTime timestamp = LocalDateTime.now();
        String flightName = "A-B@1000";

        Flight flight = new Flight();
        flight.setId(1);
        flight.setName(flightName);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setTime(timestamp);

        assertThat(flight.getId()).isEqualTo(1);
        assertThat(flight.getName()).isEqualTo(flightName);
        assertThat(flight.getOrigin()).isEqualTo(origin);
        assertThat(flight.getDestination()).isEqualTo(destination);
        assertThat(flight.getTime()).isEqualTo(timestamp);

    }
}
