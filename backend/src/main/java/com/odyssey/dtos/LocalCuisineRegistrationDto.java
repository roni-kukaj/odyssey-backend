package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record LocalCuisineRegistrationDto (
        String name, String description, MultipartFile image, Integer locationId
) { }
