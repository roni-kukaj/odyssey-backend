package com.odyssey.events;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public void registerEvent(@RequestBody EventRegistrationRequest eventRegistrationRequest){
        eventService.addEvent(eventRegistrationRequest);
    }


    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.deleteEvent(eventId);

    }

    @PutMapping("/{eventId}")
    public void updateEvent(@PathVariable("eventId") Integer eventId,
                            @RequestBody EventUpdateRequest eventUpdateRequest){
        eventService.updateEvent(eventId,eventUpdateRequest);
    }
}
