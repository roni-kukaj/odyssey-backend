package com.odyssey.tripEvents;

import java.time.LocalDate;

public record TripEventRegistrationRequest(Integer tripId, Integer eventId, LocalDate plannedDate, Integer visitOrder) {
}
