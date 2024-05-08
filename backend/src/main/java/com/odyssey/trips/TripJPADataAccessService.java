package com.odyssey.trips;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("tripJPAService")
public class TripJPADataAccessService implements TripDao {

    private final TripRepository tripRepository;

    public TripJPADataAccessService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Trip> selectAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public Optional<Trip> selectTripById(Integer id) {
        return tripRepository.findById(id);
    }

    @Override
    public List<Trip> selectTripsByUserId(Integer userId) {
        return tripRepository.findTripsByUserId(userId);
    }

    @Override
    public Optional<Trip> selectTripByUserIdAndStartDateAndEndDate(Integer userId, LocalDate startDate, LocalDate endDate) {
        return tripRepository.findTripByUserIdAndStartDateAndEndDate(userId, startDate, endDate);
    }

    @Override
    public void insertTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public boolean existsTripById(Integer id) {
        return tripRepository.existsTripById(id);
    }

    @Override
    public void deleteTripById(Integer id) {
        tripRepository.deleteById(id);
    }

    @Override
    public void updateTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public boolean existsTripByUserIdAndStartDateAndEndDate(Integer userId, LocalDate startDate, LocalDate endDate) {
        return tripRepository.existsTripByUserIdAndStartDateAndEndDate(userId, startDate, endDate);
    }
}
