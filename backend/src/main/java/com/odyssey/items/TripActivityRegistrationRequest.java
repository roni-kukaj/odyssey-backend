package com.odyssey.tripActivities;

import java.time.LocalDate;

public record TripActivityRegistrationRequest(Integer tripId, Integer activityId, LocalDate plannedDate, Integer visitOrder) {
}
