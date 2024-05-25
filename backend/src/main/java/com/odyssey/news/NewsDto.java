package com.odyssey.news;

public record NewsDto(
        Integer id,
        String title,
        String description,
        String picture,
        Integer authorId,
        String authorUsername
) {
}
