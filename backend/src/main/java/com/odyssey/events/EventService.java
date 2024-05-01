package com.odyssey.events;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventDao eventDao;


    public EventService(@Qualifier ("eventJPAService") EventDao eventDao){
        this.eventDao = eventDao;

    }

    public List<Event> getAllEvents(){
        return eventDao.selectAllEvents();
    }

    public Event getEvent(Integer id){
        return eventDao.selectEventById(id).orElseThrow(()-> new ResourceNotFoundException("event with id [%s] not found".formatted(id)));


    }


    public void addEvent(EventRegistrationRequest eventRegistrationRequest){
        if(eventDao.existsEventByName(eventRegistrationRequest.name())){
            throw new DuplicateResourceException("event already exists");
        }

        Event event = new Event(
                eventRegistrationRequest.name(), eventRegistrationRequest.description(),
                eventRegistrationRequest.image(), eventRegistrationRequest.date(),
                eventRegistrationRequest.cost(), eventRegistrationRequest.duration(),
                eventRegistrationRequest.location_id()
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


    public boolean updateEvent(Integer id, EventUpdateRequest eventUpdateRequest){
        Event event = getEvent(id);
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

        if(eventUpdateRequest.location_id()!=null && !eventUpdateRequest.location_id().equals(event.getLocation_id())){
            event.setLocation_id(eventUpdateRequest.location_id());
            changes = true;
        }

        eventDao.updateEvent(event);
        return changes;

    }

}
