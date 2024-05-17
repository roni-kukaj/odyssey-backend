package com.odyssey.user;

import org.springframework.web.multipart.MultipartFile;

public record UserRegistrationDto(
        String fullname, String username, String email, String password, MultipartFile avatar
) {
}
