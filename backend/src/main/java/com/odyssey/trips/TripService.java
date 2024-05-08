package com.odyssey.trips;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.tripActivities.*;
import com.odyssey.tripEvents.*;
import com.odyssey.tripItems.*;
import com.odyssey.tripPlaces.*;
import com.odyssey.user.User;
import com.odyssey.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TripService {

    @Autowired private TripItemService tripItemService;
    @Autowired private TripPlaceService tripPlaceService;
    @Autowired private TripActivityService tripActivityService;
    @Autowired private TripEventService tripEventService;
    @Autowired private UserService userService;

    private final TripDao tripDao;

    public TripService(
            @Qualifier("tripJPAService") TripDao tripDao
    ) {
        this.tripDao = tripDao;
    }

    private Trip getTrip(Integer id){
        return tripDao.selectTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(id)));
    }

    public List<Trip> getAllTrips() {
        return tripDao.selectAllTrips();
    }

    public List<TripGetDto> getAllTripDto() {
        List<TripGetDto> allTrips = new ArrayList<>();
        List<Trip> trips = tripDao.selectAllTrips();

        for (Trip trip: trips) {
            List<TripItemGetDto> tripItems = tripItemService.selectTripItemGetDtoByTripId(trip.getId());
            List<TripPlaceGetDto> tripPlaces = tripPlaceService.selectTripPlaceGetDtoByTripId(trip.getId());
            List<TripActivityGetDto> tripActivities = tripActivityService.selectTripActivityGetDtoByTripId(trip.getId());
            List<TripEventGetDto> tripEvents = tripEventService.selectTripEventGetDtoByTripId(trip.getId());

            allTrips.add(
                    new TripGetDto(
                            trip.getId(),
                            trip.getUser(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            tripItems,
                            tripPlaces,
                            tripActivities,
                            tripEvents
                    )
            );
        }

        return allTrips;
    }

    public TripGetDto getTripById(Integer tripId) {
        Trip trip = getTrip(tripId);
        List<TripItemGetDto> tripItemGetDto = tripItemService.selectTripItemGetDtoByTripId(tripId);
        List<TripPlaceGetDto> tripPlaceGetDto = tripPlaceService.selectTripPlaceGetDtoByTripId(tripId);
        List<TripActivityGetDto> tripActivityGetDto = tripActivityService.selectTripActivityGetDtoByTripId(tripId);
        List<TripEventGetDto> tripEventGetDto = tripEventService.selectTripEventGetDtoByTripId(tripId);

        return new TripGetDto(
                trip.getId(),
                trip.getUser(),
                trip.getStartDate(),
                trip.getEndDate(),
                tripItemGetDto,
                tripPlaceGetDto,
                tripActivityGetDto,
                tripEventGetDto
        );

    }

    public List<TripGetDto> getTripsByUserId(Integer userId) {
        User user = userService.getUser(userId);
        List<Trip> allUserTrips = tripDao.selectTripsByUserId(userId);
        List<TripGetDto> allUserTripDto = new ArrayList<>();

        for (Trip trip: allUserTrips) {
            allUserTripDto.add(
                    new TripGetDto(
                            trip.getId(),
                            trip.getUser(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            tripItemService.selectTripItemGetDtoByTripId(trip.getId()),
                            tripPlaceService.selectTripPlaceGetDtoByTripId(trip.getId()),
                            tripActivityService.selectTripActivityGetDtoByTripId(trip.getId()),
                            tripEventService.selectTripEventGetDtoByTripId(trip.getId())
                    )
            );
        }
        return allUserTripDto;
    }

    public void addTrip(TripRegistrationRequest request) {
        User user = userService.getUser(request.userId());
        Trip trip = new Trip(user, request.startDate(), request.endDate());
        if (tripDao.existsTripByUserIdAndStartDateAndEndDate(request.userId(), request.startDate(), request.endDate())){
            throw new DuplicateResourceException("trip already exists");
        }
        tripDao.insertTrip(trip);
        Trip actualTrip = tripDao.selectTripByUserIdAndStartDateAndEndDate(request.userId(), request.startDate(), request.endDate())
                .orElseThrow(() -> new ResourceNotFoundException("trip does not exist!"));

        List<TripItemPostDto> tripItemPostDtoList =
                ( request.tripItemPostDtoList() == null ? Collections.EMPTY_LIST : request.tripItemPostDtoList().stream().toList());
        List<TripPlacePostDto> tripPlacePostDtoList =
                ( request.tripPlacePostDtoList() == null ? Collections.EMPTY_LIST : request.tripPlacePostDtoList().stream().toList());
        List<TripActivityPostDto> tripActivityPostDtoList =
                ( request.tripActivityPostDtoList() == null ? Collections.EMPTY_LIST : request.tripActivityPostDtoList().stream().toList());
        List<TripEventPostDto> tripEventPostDtoList =
                ( request.tripEventPostDtoList() == null ? Collections.EMPTY_LIST : request.tripEventPostDtoList().stream().toList());

        for (TripItemPostDto tripItemPostDto: tripItemPostDtoList) {
            tripItemService.addTripItem(
                    new TripItemRegistrationRequest(
                            actualTrip.getId(),
                            tripItemPostDto.getItemId()
                    )
            );
        }
        for (TripPlacePostDto tripPlacePostDto: tripPlacePostDtoList) {
            tripPlaceService.addTripPlace(
                    new TripPlaceRegistrationRequest(
                            actualTrip.getId(),
                            tripPlacePostDto.getLocationId(),
                            tripPlacePostDto.getPlannedDate(),
                            tripPlacePostDto.getVisitOrder()
                    )
            );
        }
        for (TripActivityPostDto tripActivityPostDto: tripActivityPostDtoList) {
            tripActivityService.addTripActivity(
                    new TripActivityRegistrationRequest(
                            actualTrip.getId(),
                            tripActivityPostDto.getActivityId(),
                            tripActivityPostDto.getPlannedDate(),
                            tripActivityPostDto.getVisitOrder()
                    )
            );
        }
        for (TripEventPostDto tripEventPostDto: tripEventPostDtoList) {
            tripActivityService.addTripActivity(
                    new TripActivityRegistrationRequest(
                            actualTrip.getId(),
                            tripEventPostDto.getEventId(),
                            tripEventPostDto.getPlannedDate(),
                            tripEventPostDto.getVisitOrder()
                    )
            );
        }
    }

    public void deleteTrip(Integer tripId) {
        Trip trip = getTrip(tripId);
        tripItemService.deleteTripItemsByTripId(tripId);
        tripPlaceService.deleteTripPlacesByTripId(tripId);
        tripActivityService.deleteTripActivitiesByTripId(tripId);
        tripEventService.deleteTripEventsByTripId(tripId);
        tripDao.deleteTripById(tripId);
    }

    public void deleteAllUserTrips(Integer userId) {
        List<TripGetDto> tripGetDtoList = getTripsByUserId(userId);
        for (TripGetDto tripGetDto: tripGetDtoList) {
            deleteTrip(tripGetDto.getId());
        }
    }

}
