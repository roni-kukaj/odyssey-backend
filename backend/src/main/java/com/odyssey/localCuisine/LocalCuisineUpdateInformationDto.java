package com.odyssey.localCuisine;

import java.time.LocalDate;

public record LocalCuisineUpdateInformationDto(
        String name,
        String description,
        Integer locationId
) {
}
