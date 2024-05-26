package com.odyssey.dtos;

public record UserUpdateRequest(
        String fullname, String username, String email, String password, String avatar, Integer role_id
) {
}
