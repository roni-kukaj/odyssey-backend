package com.odyssey.hotels;

public record HotelRegistrationRequest(String name, Integer locationId, Double rating, String bookingLink) {
}
