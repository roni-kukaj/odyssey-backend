package com.odyssey.locations;

import org.springframework.web.multipart.MultipartFile;

public record LocationUpdateDto(
        String city, String country, MultipartFile file
) {
}
