package com.odyssey.tripPlaces;

import java.time.LocalDate;

public record TripPlaceRegistrationRequest (Integer tripId, Integer locationId, LocalDate plannedDate, Integer visitOrder) {
}
