package com.odyssey.dtos;

import com.odyssey.models.Role;

public record UserDto(
        Integer id,
        String fullname,
        String username,
        String email,
        String avatar,
        Role role
) {
}
