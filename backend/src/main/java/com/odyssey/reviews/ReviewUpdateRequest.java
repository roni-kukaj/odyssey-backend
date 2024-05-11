package com.odyssey.reviews;

public record ReviewUpdateRequest(String description, Integer rating,Integer userId, Integer locationId) {
}
