package com.odyssey.auth;

import com.odyssey.user.UserDto;

public record AuthenticationResponse(
        String token
) {
}
