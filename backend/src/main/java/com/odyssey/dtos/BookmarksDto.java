package com.odyssey.dtos;

import com.odyssey.models.Location;

public record BookmarksDto(
        Integer id,
        Location location,
        Integer userId,
        String username
) {
}
