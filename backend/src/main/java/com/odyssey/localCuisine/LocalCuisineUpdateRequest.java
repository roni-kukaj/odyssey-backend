package com.odyssey.localCuisine;

public record LocalCuisineUpdateRequest(
        String name, String description , String image, Integer locationId
) {
}
