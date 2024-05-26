package com.odyssey.dtos;

import com.odyssey.models.Activity;
import com.odyssey.models.Event;
import com.odyssey.models.Item;
import com.odyssey.models.Location;
import com.odyssey.dtos.UserDto;

import java.time.LocalDate;
import java.util.Set;

public record TripDto(
        Integer id,
        UserDto userDto,
        LocalDate startDate,
        LocalDate endDate,
        Set<Item> items,
        Set<Location> places,
        Set<Activity> activities,
        Set<Event> events
) {
}
