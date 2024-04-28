package com.odyssey.user;

import com.odyssey.role.Role;

public record UserUpdateRequest(
        String fullname, String username, String email, String password, String avatar, Integer role_id
) {
}
