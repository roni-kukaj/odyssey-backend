package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record LocalCuisineUpdateDto(
        String name,
        String description,
        Integer locationId,
        MultipartFile file
) {
}
