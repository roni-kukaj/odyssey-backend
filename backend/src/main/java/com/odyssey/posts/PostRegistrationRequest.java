package com.odyssey.posts;

public record PostRegistrationRequest(String text, String image, Integer userId, Integer tripId) {
}
