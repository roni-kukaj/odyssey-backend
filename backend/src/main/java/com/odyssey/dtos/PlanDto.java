package com.odyssey.dtos;

import com.odyssey.models.Location;
import com.odyssey.dtos.UserDto;

import java.time.LocalDate;

public record PlanDto(
        Integer id,
        LocalDate plannedDate,
        UserDto userDto,
        Location location
) {
}
