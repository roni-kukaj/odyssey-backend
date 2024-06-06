package com.odyssey.events;

import com.odyssey.models.Location;
import com.odyssey.models.Event;
import com.odyssey.repositories.EventRepository;
import com.odyssey.services.data.EventJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

public class EventJPADataAccessServiceTest {

    private EventJPADataAccessService eventJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        eventJPADataAccessService = new EventJPADataAccessService(eventRepository);
    }

   @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();

   }

    @Test
    void testGetAllEvents() {
        eventJPADataAccessService.selectAllEvents();
        verify(eventRepository).findAll();

    }


    @Test
    void testGetEventById() {
        int id = 1;
        eventJPADataAccessService.selectEventById(id);
        verify(eventRepository).findById(id);
    }


    @Test
    void insertEvent() {
        LocalDate date = LocalDate.of(2024,11,28);
        Location location = new Location();
        location.setId(1);

        Event event = new Event(1,"Thanksgiving", "NYC tradition","Image 1", date,75.0,1,location);
        eventJPADataAccessService.insertEvent(event);
        verify(eventRepository).save(event);
    }


    @Test
    void existsEventByName() {
        String name = "Thanksgiving";
        eventJPADataAccessService.existsEventByName(name);
        verify(eventRepository).existsEventByName(name);
    }


    @Test
    void existsEventById() {
        int id = 2;
        eventJPADataAccessService.existsEventById(id);
        verify(eventRepository).existsEventById(id);
    }

    @Test
    void testDeleteEvent() {
        int id = 1;
        eventJPADataAccessService.deleteEventById(id);
        verify(eventRepository).deleteById(id);
    }

    
}
