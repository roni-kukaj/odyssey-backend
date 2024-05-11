package com.odyssey.trips;

import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityService;
import com.odyssey.events.Event;
import com.odyssey.events.EventService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.items.Item;
import com.odyssey.items.ItemService;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationService;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import com.odyssey.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TripService {

    @Autowired private UserService userService;
    @Autowired private ItemService itemService;
    @Autowired private LocationService locationService;
    @Autowired private ActivityService activityService;
    @Autowired private EventService eventService;

    private final TripDao tripDao;
    private final UserDao userDao;

    public TripService(
            @Qualifier("tripJPAService") TripDao tripDao,
            @Qualifier("userJPAService") UserDao userDao
            ) {
        this.tripDao = tripDao;
        this.userDao = userDao;
    }

    public List<Trip> getAllTrips() {
        return tripDao.selectAllTrips();
    }

    public Trip getTrip(Integer id){
        return tripDao.selectTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(id)));
    }

    public List<Trip> getTripsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return tripDao.selectTripsByUserId(userId);
    }

    public void addTrip(TripRegistrationRequest request) {
        if (tripDao.existsTripByUserIdAndStartDate(request.userId(), request.startDate())) {
            throw new DuplicateResourceException("trip already exists");
        }
        User user = userService.getUser(request.userId());
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();

        for (Integer i: request.itemIds()) {
            items.add(itemService.getItem(i));
        }
        for (Integer i: request.placeIds()) {
            places.add(locationService.getLocation(i));
        }
        for (Integer i: request.activityIds()) {
            activities.add(activityService.getActivity(i));
        }
        for (Integer i: request.eventIds()) {
            events.add(eventService.getEvent(i));
        }

        Trip trip = new Trip(
                user, request.startDate(), request.endDate(), items, places, activities, events
        );

        tripDao.insertTrip(trip);
    }

    void deleteTripById(Integer id) {
        if (tripDao.existsTripById(id)) {
            tripDao.deleteTripById(id);
        }
        else {
            throw new ResourceNotFoundException("trip with id [%s] not found".formatted(id));
        }
    }

    public boolean updateTrip(Integer tripId, TripUpdateRequest request) {
        Trip existingTrip = getTrip(tripId);
        User user = userService.getUser(request.userId());
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();

        for (Integer i: request.itemIds()) {
            items.add(itemService.getItem(i));
        }
        for (Integer i: request.placeIds()) {
            places.add(locationService.getLocation(i));
        }
        for (Integer i: request.activityIds()) {
            activities.add(activityService.getActivity(i));
        }
        for (Integer i: request.eventIds()) {
            events.add(eventService.getEvent(i));
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
        if (!existingTrip.getUser().equals(user)) {
            throw new RequestValidationException("cannot change user");
        }

        tripDao.updateTrip(existingTrip);
        return changes;
    }

}
