package com.odyssey.dtos;

import com.odyssey.models.Location;
import com.odyssey.dtos.UserDto;

public record ReviewDto(
        Integer id,
        String description,
        Integer rating,
        UserDto userDto,
        Location location
) {
}
