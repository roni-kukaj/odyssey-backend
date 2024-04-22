package com.odyssey.locations;

public record LocationRegistrationRequest(
        Integer id, String city, String country, String picture
) {
    public LocationRegistrationRequest(String city, String country, String picture) {
        this(null, city, country, picture);
    }

}
