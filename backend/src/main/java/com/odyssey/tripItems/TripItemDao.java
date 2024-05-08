package com.odyssey.tripItems;

import java.util.List;
import java.util.Optional;

public interface TripItemDao {
    List<TripItem> selectAllTripItems();
    Optional<TripItem> selectTripItemById(Integer id);
    List<TripItem> selectTripItemsByTripId(Integer tripId);
    void insertTripItem(TripItem tripItem);
    boolean existsTripItemById(Integer id);
    boolean existsTripItemByTripIdAndItemId(Integer tripId, Integer itemId);
    void deleteTripItemById(Integer id);
    void deleteTripItemsByTripId(Integer tripId);
}
