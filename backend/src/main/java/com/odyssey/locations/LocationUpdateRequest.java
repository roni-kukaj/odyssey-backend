package com.odyssey.locations;

public record LocationUpdateRequest(
        Integer id, String city, String country
) {
}
