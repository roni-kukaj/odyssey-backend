package com.odyssey.dtos;

import com.odyssey.models.Activity;
import com.odyssey.dtos.UserDto;

public record RecommendationDto(
        Integer id,
        String description,
        UserDto userDto,
        Activity activity
) {
}
