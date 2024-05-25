package com.odyssey.trips;

import com.odyssey.activities.Activity;
import com.odyssey.events.Event;
import com.odyssey.items.Item;
import com.odyssey.locations.Location;
import com.odyssey.user.UserDto;
import org.springframework.cglib.core.Local;

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
