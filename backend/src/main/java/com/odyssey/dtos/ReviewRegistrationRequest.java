package com.odyssey.dtos;

public record ReviewRegistrationRequest(String description, Integer rating,Integer userId, Integer locationId) {
}
