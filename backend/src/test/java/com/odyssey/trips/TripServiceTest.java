package com.odyssey.trips;


import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityDao;
import com.odyssey.events.Event;
import com.odyssey.events.EventDao;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.items.Item;
import com.odyssey.items.ItemDao;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripDao tripDao;
    @Mock
    private UserDao userDao;
    @Mock
    private LocationDao locationDao;
    @Mock
    private ActivityDao activityDao;
    @Mock
    private EventDao eventDao;

    @Mock
    private ItemDao itemDao;

    private TripService underTest;

    @BeforeEach
    void setUp(){
        underTest = new TripService(tripDao, userDao, itemDao, locationDao, activityDao, eventDao);
    }

    @Test
    void getAllTrips() {
        underTest.getAllTrips();
        verify(tripDao).selectAllTrips();
    }

    @Test
    void getTrip() {
        int id = 1;
        LocalDate startDate = LocalDate.of(2024,6,25);
        LocalDate endDate = LocalDate.of(2024,7,3);
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Trip trip = new Trip(id,new User(),startDate,endDate,items,places,activities,events);
        when(tripDao.selectTripById(id)).thenReturn(Optional.of(trip));
        Trip trip1 = underTest.getTrip(id);
        assertThat(trip1).isEqualTo(trip);
    }

    @Test
    void getTripsByUserId() {
        User user = new User();
        int user_id = 1;
        user.setId(user_id);

        when(userDao.existsUserById(user_id)).thenReturn(false);
        assertThatThrownBy(()->underTest.getTripsByUserId(user_id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(user_id));
        verify(tripDao,never()).insertTrip(any());
    }


    @Test
    void willThrowWhenGetTripReturnsEmptyOptional() {
        int id = 2;
        when(tripDao.selectTripById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.getTrip(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("trip with id [%s] not found".formatted(id));

    }


    @Test
    void addTrip() {
        User user = new User();
        int user_id = 2;
        user.setId(user_id);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<Integer> itemsId = new ArrayList<>();
        List<Integer> eventsId = new ArrayList<>();
        List<Integer> activitiesId = new ArrayList<>();
        List<Integer> placesId = new ArrayList<>();

        TripRegistrationRequest tripRegistrationRequest = new TripRegistrationRequest(
                user_id, startDate, endDate, itemsId, placesId, activitiesId, eventsId
        );

        when(userDao.selectUserById(user_id)).thenReturn(Optional.of(user));
        when(tripDao.existsTripByUserIdAndStartDate(user_id, startDate)).thenReturn(false);

        underTest.addTrip(tripRegistrationRequest);

        ArgumentCaptor<Trip> tripArgumentCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripDao).insertTrip(tripArgumentCaptor.capture());

        Trip capturedTrip = tripArgumentCaptor.getValue();

        Assertions.assertThat(capturedTrip.getId()).isNull();
        Assertions.assertThat(capturedTrip.getUser().getId()).isEqualTo(tripRegistrationRequest.userId());
        Assertions.assertThat(capturedTrip.getStartDate()).isEqualTo(tripRegistrationRequest.startDate());
        Assertions.assertThat(capturedTrip.getEndDate()).isEqualTo(tripRegistrationRequest.endDate());

        Set<Integer> capturedItemIds = capturedTrip.getItems().stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedEventIds = capturedTrip.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedPlaceIds = capturedTrip.getPlaces().stream()
                .map(Location::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedActivityIds = capturedTrip.getActivities().stream()
                .map(Activity::getId)
                .collect(Collectors.toSet());
        Assertions.assertThat(capturedItemIds).isEqualTo(new HashSet<>(tripRegistrationRequest.itemIds()));
        Assertions.assertThat(capturedEventIds).isEqualTo(new HashSet<>(tripRegistrationRequest.eventIds()));
        Assertions.assertThat(capturedPlaceIds).isEqualTo(new HashSet<>(tripRegistrationRequest.placeIds()));
        Assertions.assertThat(capturedActivityIds).isEqualTo(new HashSet<>(tripRegistrationRequest.activityIds()));
    }


    @Test
    void deleteTrip() {
        int id = 5;
        when(tripDao.existsTripById(id)).thenReturn(true);
        underTest.deleteTripById(id);
        verify(tripDao).deleteTripById(id);
    }

    @Test
    void willThrowDeleteTripNotExists() {
        int id = 5;
        lenient().when(tripDao.existsTripById(id)).thenReturn(false);

        Assertions.assertThatThrownBy(()-> underTest.deleteTripById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("trip with id [%s] not found".formatted(id));

        verify(tripDao,never()).deleteTripById(any());
    }

    @Test
    void updateTrip() {
        int id=2;

        User user = new User();
        int user_id = 2;
        user.setId(user_id);

        LocalDate startDate = LocalDate.of(2024,6,26);
        LocalDate endDate = LocalDate.of(2024,7,4);
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();
        List<Integer> itemsId = new ArrayList<>();
        List<Integer> eventsId = new ArrayList<>();
        List<Integer> activitiesId = new ArrayList<>();
        List<Integer> placesId = new ArrayList<>();

       Trip trip = new Trip(user,startDate,endDate,items,places,activities,events);
       when(userDao.selectUserById(user_id)).thenReturn(Optional.of(user));
       when(tripDao.selectTripById(id)).thenReturn(Optional.of(trip));

       LocalDate startDate1 = LocalDate.of(2024,8,15);
       LocalDate endDate1 = LocalDate.of(2024,8,20);
        Set<Item> items1 = new HashSet<>();
        Set<Location> places1 = new HashSet<>();
        Set<Activity> activities1 = new HashSet<>();
        Set<Event> events1 = new HashSet<>();

        TripUpdateRequest tripUpdateRequest = new TripUpdateRequest(
                user.getId(), startDate1, endDate1, itemsId, placesId, activitiesId, eventsId
        );

        underTest.updateTrip(id, tripUpdateRequest);

        ArgumentCaptor<Trip> tripArgumentCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripDao).updateTrip(tripArgumentCaptor.capture());

        Trip capturedTrip = tripArgumentCaptor.getValue();

        Assertions.assertThat(capturedTrip.getId()).isNull();
        Assertions.assertThat(capturedTrip.getUser().getId()).isEqualTo(tripUpdateRequest.userId());
        Assertions.assertThat(capturedTrip.getStartDate()).isEqualTo(tripUpdateRequest.startDate());
        Assertions.assertThat(capturedTrip.getEndDate()).isEqualTo(tripUpdateRequest.endDate());

        Set<Integer> capturedItemIds = capturedTrip.getItems().stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedEventIds = capturedTrip.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedPlaceIds = capturedTrip.getPlaces().stream()
                .map(Location::getId)
                .collect(Collectors.toSet());

        Set<Integer> capturedActivityIds = capturedTrip.getActivities().stream()
                .map(Activity::getId)
                .collect(Collectors.toSet());
        Assertions.assertThat(capturedItemIds).isEqualTo(new HashSet<>(tripUpdateRequest.itemIds()));
        Assertions.assertThat(capturedEventIds).isEqualTo(new HashSet<>(tripUpdateRequest.eventIds()));
        Assertions.assertThat(capturedPlaceIds).isEqualTo(new HashSet<>(tripUpdateRequest.placeIds()));
        Assertions.assertThat(capturedActivityIds).isEqualTo(new HashSet<>(tripUpdateRequest.activityIds()));
    }


    @Test
    void willThrowWhenUpdateTripNoDataChanges() {
        int id = 3;
        User user = new User();
        user.setId(3);
        LocalDate startDate = LocalDate.of(2024,6,26);
        LocalDate endDate = LocalDate.of(2024,7,4);
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();

        List<Integer> itemsId = new ArrayList<>();
        List<Integer>eventsId = new ArrayList<>();
        List<Integer>activitesId = new ArrayList<>();
        List<Integer>placesId = new ArrayList<>();

       Trip trip = new Trip(user,startDate,endDate,items,places,activities,events);
        when(tripDao.selectTripById(id)).thenReturn(Optional.of(trip));
        TripUpdateRequest tripUpdateRequest = new TripUpdateRequest(trip.getUser().getId(),trip.getStartDate(),trip.getEndDate(),itemsId,placesId,activitesId,eventsId);
        lenient().when(userDao.selectUserById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(tripDao.existsTripByUserIdAndStartDate(tripUpdateRequest.userId(),tripUpdateRequest.startDate())).thenReturn(false);
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.updateTrip(id,tripUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes");
        verify(tripDao,never()).insertTrip(any());

    }
}
