package com.odyssey.events;

import com.odyssey.locations.Location;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public record EventRegistrationRequest
        (String name, String description,
         String image, LocalDate date, Double cost,
         Integer duration, Integer location_id) {
}
