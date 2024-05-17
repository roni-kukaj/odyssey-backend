package com.odyssey.events;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.fileService.FileService;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        File file = FileService.convertFile(dto.image());

        try {

            String url = cloudinaryService.uploadImage(file, "events");
            Event event = new Event(
                    dto.name(), dto.description(), url, dto.date(), dto.cost(), dto.duration(), location
            );
            eventDao.insertEvent(event);
        } catch (IOException e) {
            // TODO -> tell the user something
        }
    }

    public void addEvent(EventRegistrationRequest eventRegistrationRequest){
        if(eventDao.existsEventByName(eventRegistrationRequest.name())){
            throw new DuplicateResourceException("event already exists");
        }
        Location location = locationDao.selectLocationById(eventRegistrationRequest.location_id()).orElseThrow(()-> new ResourceNotFoundException("location with id [%s] not found".formatted(eventRegistrationRequest.location_id())));

        Event event = new Event(
                eventRegistrationRequest.name(), eventRegistrationRequest.description(),
                eventRegistrationRequest.image(), eventRegistrationRequest.date(),
                eventRegistrationRequest.cost(), eventRegistrationRequest.duration(),
                location
        );
        eventDao.insertEvent(event);
    }


    public boolean deleteEvent(Integer id){
        if(eventDao.existsEventById(id)){
            eventDao.deleteEventById(id);
        }
        else {
            throw new ResourceNotFoundException("event with id [%s] not found".formatted(id) );
        }
        return false;
    }

    public void updateEventInformation(Integer id, EventUpdateInformationDto dto) {
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

        eventDao.updateEvent(event);
    }

    public void updateEventImage(Integer id, MultipartFile image) {
        Event event = getEvent(id);
        try {
            File file = FileService.convertFile(image);
            String newUrl = cloudinaryService.uploadImage(file, "events");
            if (cloudinaryService.deleteImageByUrl(event.getImage())) {
                event.setImage(newUrl);
                eventDao.updateEvent(event);
            }
            else {
                throw new IOException();
            }
        } catch (IOException e) {
            // TODO -> tell the user something
        }
    }

    public boolean updateEvent(Integer id, EventUpdateRequest eventUpdateRequest){
        Event event = getEvent(id);
        Location location = locationDao.selectLocationById(eventUpdateRequest.location_id()).orElseThrow(()-> new ResourceNotFoundException("location with id [%s] not found".formatted(eventUpdateRequest.location_id())));
        boolean changes = false;
        if(eventUpdateRequest.name() != null && !eventUpdateRequest.name().equals(event.getName())){
            event.setName(eventUpdateRequest.name());
            changes = true;
        }

        if(eventUpdateRequest.description()!=null && !eventUpdateRequest.description().equals(event.getDescription())){
            event.setDescription(eventUpdateRequest.description());
            changes = true;
        }

        if(eventUpdateRequest.cost()!=null && !eventUpdateRequest.cost().equals(event.getCost())){
            event.setCost(eventUpdateRequest.cost());
            changes = true;
        }

        if(eventUpdateRequest.date()!=null && !eventUpdateRequest.date().equals(event.getDate())){
            event.setDate(eventUpdateRequest.date());
            changes = true;
        }

        if(eventUpdateRequest.duration()!=null && !eventUpdateRequest.duration().equals(event.getDuration())){
            event.setDuration(eventUpdateRequest.duration());
            changes = true;
        }

        if (eventUpdateRequest.image()!=null && !eventUpdateRequest.image().equals(event.getImage())){
            event.setImage(eventUpdateRequest.image());
            changes = true;
        }

        if(eventUpdateRequest.location_id() != null && !eventUpdateRequest.location_id().equals(event.getLocation().getId())){
            event.setLocation(location);
            changes = true;
        }

        eventDao.updateEvent(event);
        return changes;

    }

}
