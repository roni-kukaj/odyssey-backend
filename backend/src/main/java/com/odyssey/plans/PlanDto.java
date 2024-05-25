package com.odyssey.plans;

import com.odyssey.locations.Location;
import com.odyssey.user.UserDto;

public record PlanDto(
        Integer id,
        UserDto userDto,
        Location location
) {
}
