package com.odyssey.dtos;

public record HotelUpdateRequest(String name, Integer locationId, Double rating, String bookingLink) {
}
