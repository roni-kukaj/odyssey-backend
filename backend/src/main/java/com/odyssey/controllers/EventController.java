package com.odyssey.controllers;

import com.odyssey.models.Event;
import com.odyssey.dtos.EventRegistrationDto;
import com.odyssey.services.EventService;
import com.odyssey.dtos.EventUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable("eventId") Integer eventId){
        return eventService.getEvent(eventId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void registerEvent(@ModelAttribute EventRegistrationDto dto){
        eventService.addEvent(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.deleteEvent(eventId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{eventId}")
    public void updateEvent(
            @PathVariable("eventId") Integer eventId,
            @ModelAttribute EventUpdateDto dto
    ){
        eventService.updateEventInformation(eventId, dto);
    }

}
