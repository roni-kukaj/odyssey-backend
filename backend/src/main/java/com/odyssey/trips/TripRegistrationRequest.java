package com.odyssey.trips;

import com.odyssey.tripActivities.TripActivityPostDto;
import com.odyssey.tripEvents.TripEventPostDto;
import com.odyssey.tripItems.TripItemPostDto;
import com.odyssey.tripPlaces.TripPlacePostDto;

import java.time.LocalDate;
import java.util.List;

public record TripRegistrationRequest(
        Integer userId,
        LocalDate startDate,
        LocalDate endDate,
        List<TripItemPostDto> tripItemPostDtoList,
        List<TripPlacePostDto> tripPlacePostDtoList,
        List<TripActivityPostDto> tripActivityPostDtoList,
        List<TripEventPostDto> tripEventPostDtoList
) {
}
