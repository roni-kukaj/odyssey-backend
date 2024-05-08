package com.odyssey.tripItems;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("tripItemJPAService")
public class TripItemJPADataAccessService implements TripItemDao {

    private final TripItemRepository tripItemRepository;

    public TripItemJPADataAccessService(TripItemRepository tripItemRepository) {
        this.tripItemRepository = tripItemRepository;
    }

    @Override
    public List<TripItem> selectAllTripItems() {
        return tripItemRepository.findAll();
    }

    @Override
    public Optional<TripItem> selectTripItemById(Integer id) {
        return tripItemRepository.findById(id);
    }

    @Override
    public List<TripItem> selectTripItemsByTripId(Integer tripId) {
        return tripItemRepository.findTripItemsByTripId(tripId);
    }

    @Override
    public void insertTripItem(TripItem tripItem) {
        tripItemRepository.save(tripItem);
    }

    @Override
    public boolean existsTripItemById(Integer id) {
        return tripItemRepository.existsTripItemById(id);
    }

    @Override
    public boolean existsTripItemByTripIdAndItemId(Integer tripId, Integer itemId) {
        return tripItemRepository.existsTripItemByTripIdAndItemId(tripId, itemId);
    }

    @Override
    public void deleteTripItemById(Integer id) {
        tripItemRepository.deleteById(id);
    }

    @Override
    public void deleteTripItemsByTripId(Integer tripId) {
        tripItemRepository.deleteTripItemsByTripId(tripId);
    }
}
