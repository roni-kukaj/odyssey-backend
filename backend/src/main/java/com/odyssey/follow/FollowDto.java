package com.odyssey.follow;

import com.odyssey.user.UserDto;

public record FollowDto(
        Integer id,
        UserDto follower,
        UserDto following
) {
}
