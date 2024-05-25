package com.odyssey.trips;

import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityDao;
import com.odyssey.activities.ActivityService;
import com.odyssey.events.Event;
import com.odyssey.events.EventDao;
import com.odyssey.events.EventService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.items.Item;
import com.odyssey.items.ItemDao;
import com.odyssey.items.ItemService;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.locations.LocationService;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import com.odyssey.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripDao tripDao;
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final LocationDao locationDao;
    private final ActivityDao activityDao;
    private final EventDao eventDao;
    private final TripDtoMapper tripDtoMapper;

    public TripService(
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("itemJPAService") ItemDao itemDao,
            @Qualifier("locationJPAService") LocationDao locationDao,
            @Qualifier("activityJPAService") ActivityDao activityDao,
            @Qualifier("eventJPAService") EventDao eventDao,
            TripDtoMapper tripDtoMapper
    ) {
        this.tripDao = tripDao;
        this.userDao = userDao;
        this.itemDao = itemDao;
        this.locationDao = locationDao;
        this.activityDao = activityDao;
        this.eventDao = eventDao;
        this.tripDtoMapper = tripDtoMapper;
    }

    public List<TripDto> getAllTrips() {
        return tripDao.selectAllTrips()
                .stream().map(tripDtoMapper).collect(Collectors.toList());
    }

    private Trip getTripById(Integer id){
        return tripDao.selectTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(id)));
    }

    public TripDto getTrip(Integer id){
        return tripDao.selectTripById(id)
                .map(tripDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(id)));
    }

    public List<TripDto> getTripsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return tripDao.selectTripsByUserId(userId)
                .stream().map(tripDtoMapper).collect(Collectors.toList());
    }

    public void addTrip(TripRegistrationRequest request) {
        if (tripDao.existsTripByUserIdAndStartDate(request.userId(), request.startDate())) {
            throw new DuplicateResourceException("trip already exists");
        }
        User user = userDao.selectUserById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.userId())));

        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();

        for (Integer i: request.itemIds()) {
            items.add(itemDao.selectItemById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("item with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.placeIds()) {
            places.add(locationDao.selectLocationById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.activityIds()) {
            activities.add(activityDao.selectActivityById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("activity with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.eventIds()) {
            events.add(eventDao.selectEventById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("event with id [%s] not found".formatted(i))));
        }

        Trip trip = new Trip(
                user, request.startDate(), request.endDate(), items, places, activities, events
        );

        tripDao.insertTrip(trip);
    }

    public void deleteTripById(Integer id) {
        if (tripDao.existsTripById(id)) {
            tripDao.deleteTripById(id);
        }
        else {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(id));
        }
    }

    public void updateTrip(Integer tripId, TripUpdateRequest request) {
        Trip existingTrip = getTripById(tripId);
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();

        for (Integer i: request.itemIds()) {
            items.add(itemDao.selectItemById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("item with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.placeIds()) {
            places.add(locationDao.selectLocationById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.activityIds()) {
            activities.add(activityDao.selectActivityById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("activity with id [%s] not found".formatted(i))));
        }
        for (Integer i: request.eventIds()) {
            events.add(eventDao.selectEventById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("event with id [%s] not found".formatted(i))));
        }

        boolean changes = false;
        if (request.startDate() != null && !request.startDate().equals(existingTrip.getStartDate())) {
            existingTrip.setStartDate(request.startDate());
            changes = true;
        }
        if (request.endDate() != null && !request.endDate().equals(existingTrip.getEndDate())) {
            existingTrip.setEndDate(request.endDate());
            changes = true;
        }
        if (!items.equals(existingTrip.getItems())) {
            existingTrip.setItems(items);
            changes = true;
        }
        if (!places.equals(existingTrip.getPlaces())) {
            existingTrip.setPlaces(places);
            changes = true;
        }
        if (!activities.equals(existingTrip.getActivities())) {
            existingTrip.setActivities(activities);
            changes = true;
        }
        if (!events.equals(existingTrip.getEvents())) {
            existingTrip.setEvents(events);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        tripDao.updateTrip(existingTrip);
    }

}
