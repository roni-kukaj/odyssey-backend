package com.odyssey.events;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record EventRegistrationDto (
        String name,
        String description,
        MultipartFile file,
        LocalDate date,
        Double cost,
        Integer duration,
        Integer locationId
) { }