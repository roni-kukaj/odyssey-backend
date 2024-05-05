package com.odyssey.reviews;

public record ReviewUpdateRequest(String description, Integer rating,Integer user_id, Integer location_id) {
}
