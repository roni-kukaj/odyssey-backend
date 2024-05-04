package com.odyssey.hotels;

public record HotelUpdateRequest(String name, Integer locationId, Double rating, String bookingLink) {
}
