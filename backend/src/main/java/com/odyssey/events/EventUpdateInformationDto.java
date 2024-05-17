package com.odyssey.events;

import java.time.LocalDate;

public record EventUpdateInformationDto(
        String name,
        String description,
        LocalDate date,
        Double cost,
        Integer duration,
        Integer locationId
) { }