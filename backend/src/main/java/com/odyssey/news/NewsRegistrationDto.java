package com.odyssey.news;

import org.springframework.web.multipart.MultipartFile;

public record NewsRegistrationDto(
        String title, String description, MultipartFile image, Integer authorId
) { }