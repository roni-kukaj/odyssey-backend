package com.odyssey.posts;

import com.odyssey.trips.TripDto;

import java.time.LocalDate;

public record PostDto(
        Integer id,
        String text,
        String image,
        LocalDate postedTime,
        TripDto tripDto
) {
}
