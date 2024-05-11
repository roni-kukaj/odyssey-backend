package com.odyssey.reviews;

public record ReviewRegistrationRequest(String description, Integer rating,Integer userId, Integer locationId) {
}
