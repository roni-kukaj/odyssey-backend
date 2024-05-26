package com.odyssey.dtos;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record LocationRegistrationDto(
        @RequestParam("city") String city,
        @RequestParam("country") String country,
        @RequestPart("file") MultipartFile file
) {
}
