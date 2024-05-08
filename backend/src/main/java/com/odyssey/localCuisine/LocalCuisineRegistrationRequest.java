package com.odyssey.localCuisine;

public record LocalCuisineRegistrationRequest (
    String name, String description , String image, Integer locationId
){}
