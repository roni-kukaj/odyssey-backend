package com.odyssey.events;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    public void registerEvent(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart("image") MultipartFile image,
            @RequestParam("date") LocalDate date,
            @RequestParam("cost") Double cost,
            @RequestParam("duration") Integer duration,
            @RequestParam("locationId") Integer locationId
    ){
        eventService.addEvent(new EventRegistrationDto(
                name, description, image, date, cost, duration, locationId
        ));
    }


    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public void updateEventInformation(@PathVariable("eventId") Integer eventId,
                            @RequestBody EventUpdateInformationDto dto){
        eventService.updateEventInformation(eventId, dto);
    }

    @PutMapping(value = "/{eventId}/picture", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void updateEventPicture(
            @PathVariable("eventId") Integer eventId,
            @PathParam("file") MultipartFile file
    ) {
        eventService.updateEventImage(eventId, file);
    }
}
