package com.odyssey.user;

import com.odyssey.role.Role;

public record UserDto(
        Integer id,
        String fullname,
        String username,
        String email,
        String avatar,
        Role role
) {
}
