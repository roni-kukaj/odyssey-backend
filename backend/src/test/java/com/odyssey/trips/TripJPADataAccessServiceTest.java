package com.odyssey.trips;


import com.odyssey.models.*;
import com.odyssey.models.User;
import com.odyssey.repositories.TripRepository;
import com.odyssey.services.data.TripJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.verify;

public class TripJPADataAccessServiceTest {

    private TripJPADataAccessService tripJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private TripRepository tripRepository;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        tripJPADataAccessService = new TripJPADataAccessService(tripRepository);

    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void SelectAllTrips() {
        tripJPADataAccessService.selectAllTrips();
        verify(tripRepository).findAll();
    }

    @Test
    void selectTripById() {
        int id = 1;
        tripJPADataAccessService.selectTripById(id);
        verify(tripRepository).findById(id);
    }

    @Test
    void selectTripByUserId() {
        int user_id = 1;
        tripJPADataAccessService.selectTripsByUserId(user_id);
        verify(tripRepository).findTripsByUserId(user_id);
    }

    @Test
    void insertTrip() {
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Trip trip = new Trip(new User(), LocalDate.of(2024,8,8),LocalDate.of(2024,8,12),items,places,activities,events);
        tripJPADataAccessService.insertTrip(trip);
        verify(tripRepository).save(trip);

    }

    @Test
    void existsTripById() {
        int id = 1;
        tripJPADataAccessService.existsTripById(id);
        verify(tripRepository).existsTripById(id);
    }

    @Test
    void existsTripByUserIdAndStartDate() {
        int user_id = 1;
        LocalDate startDate = LocalDate.of(2024,8,8);
        tripJPADataAccessService.existsTripByUserIdAndStartDate(user_id,startDate);
        verify(tripRepository).existsTripByUserIdAndStartDate(user_id,startDate);
    }

    @Test
    void deleteTripById() {
        int id = 4;
        tripJPADataAccessService.deleteTripById(id);
        verify(tripRepository).deleteById(id);
    }

    @Test
    void updateTrip() {
        Set<Item> items = new HashSet<>();
        Set<Location> places = new HashSet<>();
        Set<Activity> activities = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Trip trip = new Trip(new User(), LocalDate.of(2024,8,9),LocalDate.of(2024,8,13),items,places,activities,events);
        tripJPADataAccessService.updateTrip(trip);
        verify(tripRepository).save(trip);


    }
}
