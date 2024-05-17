package com.odyssey.user;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateInformationDto(
        String fullname, String username, String password
) {
}
