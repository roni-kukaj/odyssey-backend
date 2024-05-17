package com.odyssey.trips;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record TripUpdateRequest(
        LocalDate startDate,
        LocalDate endDate,
        List<Integer> itemIds,
        List<Integer> placeIds,
        List<Integer> activityIds,
        List<Integer> eventIds
) {
    public TripUpdateRequest {
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
