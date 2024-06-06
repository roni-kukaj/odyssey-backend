package com.odyssey.auth;

import com.odyssey.dtos.UserDto;

public record AuthenticationResponse(
        String token,
        UserDto userDto
) {
}
