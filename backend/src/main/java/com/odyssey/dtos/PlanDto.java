package com.odyssey.dtos;

import com.odyssey.models.Location;
import com.odyssey.dtos.UserDto;

public record PlanDto(
        Integer id,
        UserDto userDto,
        Location location
) {
}
