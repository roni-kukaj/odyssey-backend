package com.odyssey.user;

public record UserUpdateRequest(
        String fullname, String username, String email, String password, Integer location, String avatar
) {
}
