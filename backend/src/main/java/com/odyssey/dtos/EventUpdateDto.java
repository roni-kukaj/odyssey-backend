package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record EventUpdateDto(
        String name,
        String description,
        LocalDate date,
        Double cost,
        Integer duration,
        Integer locationId,
        MultipartFile file
) { }