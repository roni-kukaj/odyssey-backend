package com.odyssey.dtos;

public record NewsDto(
        Integer id,
        String title,
        String description,
        String picture,
        Integer authorId,
        String authorUsername
) {
}
