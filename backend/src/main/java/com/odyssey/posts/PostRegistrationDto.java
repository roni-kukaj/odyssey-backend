package com.odyssey.posts;

import org.springframework.web.multipart.MultipartFile;

public record PostRegistrationDto(
        String text, Integer userId, Integer tripId, MultipartFile image
) { }