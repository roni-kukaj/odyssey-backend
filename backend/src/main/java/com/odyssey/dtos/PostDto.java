package com.odyssey.dtos;

import com.odyssey.dtos.TripDto;

import java.time.LocalDate;

public record PostDto(
        Integer id,
        String text,
        String image,
        LocalDate postedTime,
        UserDto userDto,
        TripDto tripDto
) {
}
