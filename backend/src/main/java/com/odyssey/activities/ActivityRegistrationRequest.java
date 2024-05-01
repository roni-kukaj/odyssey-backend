package com.odyssey.activities;

public record ActivityRegistrationRequest(
        String name, String description, Integer cost, Integer duration, Integer locationId
) {

}
