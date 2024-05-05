package com.odyssey.flights;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;

import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class FlightService {
    private final FlightDao flightDao;
    private final LocationDao locationDao;

    public FlightService(@Qualifier("flightJPAService") FlightDao flightDao, @Qualifier("LocationJPAService") LocationDao locationDao){
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

    public List<Flight> getFlightsByOriginId(Integer origin) {
        if (!locationDao.existsLocationById(origin)) {
            throw new ResourceNotFoundException("origin with id [%s] not found".formatted(origin));
        }
        return flightDao.selectFlightsByOrigin(origin);
    }

    public List<Flight> getFlightsByDestinationId(Integer destination) {
        if (!locationDao.existsLocationById(destination)) {
            throw new ResourceNotFoundException("destination with id [%s] not found".formatted(destination));
        }
        return flightDao.selectFlightsByDestination(destination);
    }

    public void addFlight(FlightRegistrationRequest request) {
        if (flightDao.existsFlightByName(request.name())){
            throw new DuplicateResourceException("flight already exists");
        }
        Location origin = locationDao.selectLocationById(request.origin())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.origin())));

        Location destination = locationDao.selectLocationById(request.destination())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.destination())));

        Flight flight = new Flight(
                request.name(),
                request.time(),
                origin,
                destination
        );

        flightDao.insertFlight(flight);
    }

    public boolean deleteFlight(Integer id) {
        if (flightDao.existsFlightById(id)) {
            flightDao.deleteFlightById(id);
        }
        else {
            throw new ResourceNotFoundException("flight with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean updateFlight(Integer id, FlightUpdateRequest request) {
        Flight existingFlight = getFlight(id);

        if (flightDao.existsFlightByName(request.name())) {
            throw new DuplicateResourceException("flight already exists");
        }

        Location origin = locationDao.selectLocationById(request.origin())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.origin())));

        Location destination = locationDao.selectLocationById(request.destination())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.destination())));

        boolean changes = false;

        if (request.name() != null && !request.name().equals(existingFlight.getName())) {
            existingFlight.setName(request.name());
            changes = true;
        }
        if (request.origin() != null && !request.origin().equals(existingFlight.getOrigin().getId())) {
            existingFlight.setOrigin(origin);
            changes = true;
        }
        if (request.destination() != null && !request.destination().equals(existingFlight.getDestination().getId())) {
            existingFlight.setDestination(destination);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        flightDao.updateFlight(existingFlight);
        return changes;
    }
}
