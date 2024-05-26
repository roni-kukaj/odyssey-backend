package com.odyssey.services;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.EventDao;
import com.odyssey.dtos.EventRegistrationDto;
import com.odyssey.dtos.EventUpdateDto;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.services.file.FileService;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Event;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EventService {

    private final EventDao eventDao;
    private final LocationDao locationDao;
    private final CloudinaryService cloudinaryService;

    public EventService(@Qualifier ("eventJPAService") EventDao eventDao, @Qualifier("locationJPAService") LocationDao locationDao, CloudinaryService cloudinaryService){
        this.eventDao = eventDao;
        this.locationDao = locationDao;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Event> getAllEvents(){
        return eventDao.selectAllEvents();
    }

    public Event getEvent(Integer id){
        return eventDao.selectEventById(id).orElseThrow(()-> new ResourceNotFoundException("event with id [%s] not found".formatted(id)));
    }

    public void addEvent(EventRegistrationDto dto) {
        if (eventDao.existsEventByName(dto.name())) {
            throw new DuplicateResourceException("event already exists");
        }

        Location location = locationDao.selectLocationById(dto.locationId())
                .orElseThrow(()-> new ResourceNotFoundException("location with id [%s] not found".formatted(dto.locationId())));

        File file = FileService.convertFile(dto.file());

        try {
            String url = cloudinaryService.uploadImage(file, "events");
            Event event = new Event(
                    dto.name(), dto.description(), url, dto.date(), dto.cost(), dto.duration(), location
            );
            eventDao.insertEvent(event);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deleteEvent(Integer id){
        if (eventDao.existsEventById(id)) {
            eventDao.deleteEventById(id);
        }
        else {
            throw new ResourceNotFoundException("event with id [%s] not found".formatted(id) );
        }
    }

    public void updateEventInformation(Integer id, EventUpdateDto dto) {
        Event event = getEvent(id);
        Location location = locationDao.selectLocationById(dto.locationId())
                .orElseThrow(()-> new ResourceNotFoundException("location with id [%s] not found".formatted(dto.locationId())));

        boolean changes = false;

        if(dto.name() != null && !dto.name().equals(event.getName())){
            event.setName(dto.name());
            changes = true;
        }

        if(dto.description() != null && !dto.description().equals(event.getDescription())){
            event.setDescription(dto.description());
            changes = true;
        }

        if(dto.cost() != null && !dto.cost().equals(event.getCost())){
            event.setCost(dto.cost());
            changes = true;
        }

        if(dto.date() != null && !dto.date().equals(event.getDate())){
            event.setDate(dto.date());
            changes = true;
        }

        if(dto.duration() != null && !dto.duration().equals(event.getDuration())){
            event.setDuration(dto.duration());
            changes = true;
        }

        if(dto.locationId() != null && !dto.locationId().equals(event.getLocation().getId())){
            event.setLocation(location);
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        try {
            File file = FileService.convertFile(dto.file());
            String newUrl = cloudinaryService.uploadImage(file, "events");
            cloudinaryService.deleteImageByUrl(event.getImage());
            event.setImage(newUrl);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
        eventDao.updateEvent(event);
    }
}
