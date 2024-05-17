package com.odyssey.localCuisine;

import org.springframework.web.multipart.MultipartFile;

public record LocalCuisineRegistrationDto (
        String name, String description, MultipartFile image, Integer locationId
) { }
