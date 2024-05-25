package com.odyssey.bookmarks;

import com.odyssey.locations.Location;

public record BookmarksDto(
        Integer id,
        Location location,
        Integer userId,
        String username
) {
}
