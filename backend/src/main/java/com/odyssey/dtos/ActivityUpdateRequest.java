package com.odyssey.dtos;

public record ActivityUpdateRequest(
        String name, String description, Integer cost, Integer duration, Integer locationId
) {
}
