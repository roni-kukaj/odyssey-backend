package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record NewsRegistrationDto(
        String title, String description, MultipartFile image, Integer authorId
) { }