package com.odyssey.dtos;

import org.springframework.web.multipart.MultipartFile;

public record NewsUpdateDto(
        String title, String description, MultipartFile file
) { }