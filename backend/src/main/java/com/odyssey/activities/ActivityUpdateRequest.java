package com.odyssey.activities;

public record ActivityUpdateRequest(
        String name, String description, Integer cost, Integer duration, Integer locationId
) {
}
