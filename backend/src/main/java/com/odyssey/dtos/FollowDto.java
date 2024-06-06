package com.odyssey.dtos;

import com.odyssey.dtos.UserDto;

public record FollowDto(
        Integer id,
        UserDto follower,
        UserDto following
) {
}
