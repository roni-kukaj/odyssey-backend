package com.odyssey.dtos;

public record UserRegistrationRequest(
        String fullname, String username, String email, String password
) {
}
