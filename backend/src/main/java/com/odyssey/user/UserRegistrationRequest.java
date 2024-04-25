package com.odyssey.user;

public record UserRegistrationRequest(
        String fullname, String username, String email, String password, String location, String avatar
) {
}
