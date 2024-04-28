package com.odyssey.locations;

public record LocationUpdateRequest(
        String city, String country, String picture
) {
}
