package com.odyssey.locations;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record LocationRegistrationDto(
        String city, String country, MultipartFile file
) {
}
