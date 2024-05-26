package com.odyssey.services.utils;

import com.odyssey.dtos.TripDto;
import com.odyssey.models.Trip;
import com.odyssey.services.utils.UserDtoMapper;
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
