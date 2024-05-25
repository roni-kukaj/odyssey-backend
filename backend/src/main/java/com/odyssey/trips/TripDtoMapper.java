package com.odyssey.trips;

import com.odyssey.user.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TripDtoMapper implements Function<Trip, TripDto> {

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public TripDto apply(Trip trip) {
        return new TripDto(
                trip.getId(),
                userDtoMapper.apply(trip.getUser()),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getItems(),
                trip.getPlaces(),
                trip.getActivities(),
                trip.getEvents()
        );
    }
}
