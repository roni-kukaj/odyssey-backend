package com.odyssey.posts;

import org.springframework.web.multipart.MultipartFile;

public record PostUpdateDto(
        String text, MultipartFile file
) {
}
