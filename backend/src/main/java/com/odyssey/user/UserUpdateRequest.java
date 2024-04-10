package com.odyssey.user;

public record UserUpdateRequest(
        String fullname, String username, String email, String password, String location
) {
}
