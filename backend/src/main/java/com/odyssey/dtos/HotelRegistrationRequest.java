package com.odyssey.dtos;

public record HotelRegistrationRequest(String name, Integer locationId, Double rating, String bookingLink) {
}
