package com.odyssey.recommendations;

import com.odyssey.activities.Activity;
import com.odyssey.user.UserDto;

public record RecommendationDto(
        Integer id,
        String description,
        UserDto userDto,
        Activity activity
) {
}
