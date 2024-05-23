package com.odyssey.events;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void registerEvent(@ModelAttribute EventRegistrationDto dto){
        eventService.addEvent(dto);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public void updateEvent(
            @PathVariable("eventId") Integer eventId,
            @ModelAttribute EventUpdateDto dto
    ){
        eventService.updateEventInformation(eventId, dto);
    }

}
