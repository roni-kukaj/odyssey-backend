package com.odyssey.tripItems;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripItemRepository extends JpaRepository<TripItem, Integer> {
    boolean existsTripItemById(Integer id);
    boolean existsTripItemByTripIdAndItemId(Integer tripId, Integer itemId);
    List<TripItem> findTripItemsByTripId(Integer tripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TripItem t WHERE t.trip.id = :tripId")
    void deleteTripItemsByTripId(Integer tripId);
}
