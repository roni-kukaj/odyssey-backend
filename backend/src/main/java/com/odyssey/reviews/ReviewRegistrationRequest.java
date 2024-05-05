package com.odyssey.reviews;

public record ReviewRegistrationRequest(String description, Integer rating,Integer user_id, Integer location_id) {
}
