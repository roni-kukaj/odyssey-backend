package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record PostUpdateDto(
        String text, MultipartFile file
) {
}
