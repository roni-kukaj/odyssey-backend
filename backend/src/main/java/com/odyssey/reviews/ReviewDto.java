package com.odyssey.reviews;

import com.odyssey.locations.Location;
import com.odyssey.user.UserDto;

public record ReviewDto(
        Integer id,
        String description,
        Integer rating,
        UserDto userDto,
        Location location
) {
}
