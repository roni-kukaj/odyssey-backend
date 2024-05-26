package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateDto(
        String fullname, String username, String password, MultipartFile file
) {
}
