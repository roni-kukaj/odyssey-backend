package com.odyssey.tripItems;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.items.Item;
import com.odyssey.items.ItemDao;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripItemService {

    private final TripItemDao tripItemDao;
    private final TripDao tripDao;
    private final ItemDao itemDao;

    public TripItemService(
            @Qualifier("tripItemJPAService") TripItemDao tripItemDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("itemJPAService") ItemDao itemDao
            ) {
        this.tripItemDao = tripItemDao;
        this.tripDao = tripDao;
        this.itemDao = itemDao;
    }

    public List<TripItem> getAllTripItems() {
        return tripItemDao.selectAllTripItems();
    }

    public TripItem getTripItemById(Integer id) {
        return tripItemDao.selectTripItemById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip item with id [%s] not found".formatted(id)));
    }

    public List<TripItem> getTripItemsByTripId(Integer tripId) {
        return tripItemDao.selectTripItemsByTripId(tripId);
    }

    public void addTripItem(TripItemRegistrationRequest request) {
        Item item = itemDao.selectItemById(request.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("item with id [%s] not found".formatted(request.itemId())));
        Trip trip = tripDao.selectTripById(request.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(request.tripId())));

        TripItem tripItem = new TripItem(trip, item);

        if (tripItemDao.existsTripItemByTripIdAndItemId(request.tripId(), request.itemId())) {
            throw new DuplicateResourceException("item with id [%s] already added to trip with id [%s]".formatted(request.itemId(), request.tripId()));
        }

        tripItemDao.insertTripItem(tripItem);
    }

    public boolean deleteTripItem(Integer id) {
        if (tripItemDao.existsTripItemById(id)) {
            tripItemDao.deleteTripItemById(id);
        }
        else {
            throw new ResourceNotFoundException("trip item with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean deleteTripItemsByTripId(Integer tripId) {
        if (!tripDao.existsTripById(tripId)) {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(tripId));
        }
        tripItemDao.deleteTripItemsByTripId(tripId);
        return false;
    }
}