package com.odyssey.flights;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;

import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {
    private final FlightDao flightDao;
    private final LocationDao locationDao;

    public FlightService(
            @Qualifier("flightJPAService") FlightDao flightDao,
            @Qualifier("locationJPAService") LocationDao locationDao){
        this.flightDao = flightDao;
        this.locationDao = locationDao;
    }

    public List<Flight> getAllFlights() {
        return flightDao.selectAllFlights();
    }

    public Flight getFlight(Integer id) {
        return flightDao.selectFlightById(id)
                .orElseThrow(() -> new ResourceNotFoundException("flight with id [%s] not found".formatted(id)));
    }

    public List<Flight> getFlightsByOriginId(Integer originId) {
        if (!locationDao.existsLocationById(originId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(originId));
        }
        return flightDao.selectFlightsByOriginId(originId);
    }

    public List<Flight> getFlightsByDestinationId(Integer destinationId) {
        if (!locationDao.existsLocationById(destinationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(destinationId));
        }
        return flightDao.selectFlightsByDestinationId(destinationId);
    }

    public void addFlight(FlightRegistrationRequest request) {
        Location origin = locationDao.selectLocationById(request.originId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.originId())));

        Location destination = locationDao.selectLocationById(request.destinationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.destinationId())));

        if (origin.equals(destination)) {
            throw new RequestValidationException("origin and destination must be different");
        }

        String flightName = FlightNamingService.getFlightName(origin, destination, request.departure());

        if (flightDao.existsFlightByName(flightName)){
            throw new DuplicateResourceException("flight already exists");
        }

        Flight flight = new Flight(
                flightName, request.departure(), origin, destination
        );

        flightDao.insertFlight(flight);
    }

    public void deleteFlight(Integer id) {
        if (flightDao.existsFlightById(id)) {
            flightDao.deleteFlightById(id);
        }
        else {
            throw new ResourceNotFoundException("flight with id [%s] not found".formatted(id));
        }
    }

    public void updateFlight(Integer id, FlightUpdateRequest request) {
        Flight existingFlight = getFlight(id);

        Location origin = locationDao.selectLocationById(request.originId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.originId())));

        Location destination = locationDao.selectLocationById(request.destinationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.destinationId())));

        String flightName = FlightNamingService.getFlightName(origin, destination, request.departure());

        boolean changes = false;

        if (flightDao.existsFlightByName(flightName)) {
            throw new DuplicateResourceException("flight already exists");
        }

        if (!flightName.equals(existingFlight.getName())) {
            existingFlight.setName(flightName);
            changes = true;
        }
        if (request.originId() != null && !request.originId().equals(existingFlight.getOrigin().getId())) {
            existingFlight.setOrigin(origin);
            changes = true;
        }
        if (request.destinationId() != null && !request.destinationId().equals(existingFlight.getDestination().getId())) {
            existingFlight.setDestination(destination);
            changes = true;
        }
        if (request.departure() != null && !request.departure().equals(existingFlight.getTime())) {
            existingFlight.setTime(request.departure());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        flightDao.updateFlight(existingFlight);
    }
}
