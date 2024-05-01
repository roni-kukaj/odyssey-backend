package com.odyssey.events;

import com.odyssey.locations.Location;

import java.time.LocalDate;
import java.util.Date;

public record EventUpdateRequest(
        String name, String description,
        String image, LocalDate date, Double cost,
        Integer duration, Location location_id
) {
}
