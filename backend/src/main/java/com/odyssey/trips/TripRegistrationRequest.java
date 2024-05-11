package com.odyssey.trips;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record TripRegistrationRequest(
        Integer userId,
        LocalDate startDate,
        LocalDate endDate,
        List<Integer> itemIds,
        List<Integer> placeIds,
        List<Integer> activityIds,
        List<Integer> eventIds
) {
    public TripRegistrationRequest {
        if (itemIds == null) {
            itemIds = new ArrayList<>();
        }
        if (placeIds == null) {
            placeIds = new ArrayList<>();
        }
        if (activityIds == null) {
            activityIds = new ArrayList<>();
        }
        if (eventIds == null) {
            eventIds = new ArrayList<>();
        }
    }
}
