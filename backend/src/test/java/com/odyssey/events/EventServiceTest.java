package com.odyssey.events;


import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventDao eventDao;
    @Mock
    private LocationDao locationDao;
    private EventService underTest;

    @BeforeEach
    void setUp(){
        underTest = new EventService(eventDao,locationDao);
    }


    @Test
    void getAllEvents() {
        underTest.getAllEvents();
        verify(eventDao).selectAllEvents();
    }




    @Test
    void testGetEventById() {
        int id = 1;
        LocalDate date = LocalDate.of(2024, 11, 28);
        Location location = new Location();
        location.setId(1);

        Event event = new Event(id, "Thanksgiving", "NYC tradition", "Picture 1",date,75.0,2,location);
        when(eventDao.selectEventById(id)).thenReturn(Optional.of(event));

        Event actual = underTest.getEvent(id);
        assertThat(actual).isEqualTo(event);
    }


    @Test
    void willThrowWhenGetEventReturnEmptyOptional() {
        int id = 1;
        when(eventDao.selectEventById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> underTest.getEvent(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("event with id [%s] not found".formatted(id));
    }


    @Test
    void testAddEvent() {
        String name = "Thanksgiving";
        String description = "NYC tradition";
        LocalDate date = LocalDate.of(2024,11,28);
        Location location = new Location();
        location.setId(1);
        Double cost = 75.0;
        Integer duration = 2;
        String image="Picture 1";
        when(eventDao.existsEventByName(name)).thenReturn(false);
        when(locationDao.selectLocationById(1)).thenReturn(Optional.of(location));
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(
                name,description,image,date,cost,duration,location.getId()
        );
        underTest.addEvent(eventRegistrationRequest);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventDao).insertEvent(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent.getId()).isNull();
        assertThat(capturedEvent.getName()).isEqualTo(eventRegistrationRequest.name());
        assertThat(capturedEvent.getDescription()).isEqualTo(eventRegistrationRequest.description());
        assertThat(capturedEvent.getImage()).isEqualTo(eventRegistrationRequest.image());
        assertThat(capturedEvent.getDuration()).isEqualTo(eventRegistrationRequest.duration());
        assertThat(capturedEvent.getDate()).isEqualTo(eventRegistrationRequest.date());
        assertThat(capturedEvent.getLocation().getId()).isEqualTo(eventRegistrationRequest.location_id());
        assertThat(capturedEvent.getCost()).isEqualTo(eventRegistrationRequest.cost());

    }


    @Test
    void willThrowWhenNameExistsWhileAddingEvent() {
        String name = "Thanksgiving";
        String description = "NYC tradition";
        LocalDate date = LocalDate.of(2024,11,28);
        Location location = new Location();
        location.setId(1);
        Double cost = 75.0;
        Integer duration = 2;
        String image="Picture 1";
        when(eventDao.existsEventByName(name)).thenReturn(true);
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(
                name,description,image,date,cost,duration,location.getId()
        );
        assertThatThrownBy(()-> underTest.addEvent(eventRegistrationRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("event already exists");

    }

    @Test
    void deleteEventById() {
        int id = 2;
        when(eventDao.existsEventById(id)).thenReturn(true);
        underTest.deleteEvent(id);
        verify(eventDao).deleteEventById(id);
    }

    @Test
    void willThrowDeleteEventByIdNotExists() {
        int id = 2;
        when(eventDao.existsEventById(id)).thenReturn(false);
        assertThatThrownBy(()->underTest.deleteEvent(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("event with id [%s] not found".formatted(id));
        verify(eventDao, never()).deleteEventById(any());

    }

    @Test
    void canUpdateAllEventProperties() {
        int id = 2;
        String name = "Thanksgiving";
        String description = "NYC tradition";
        LocalDate date = LocalDate.of(2024,11,28);
        Location location = new Location();
        location.setId(1);
        Double cost = 75.0;
        Integer duration = 2;
        String image="Picture 1";

        Event event = new Event(name,description,image,date,cost,duration,location);
        when(eventDao.selectEventById(id)).thenReturn(Optional.of(event));

        String newEvent = "Thanksgiving Day";
        String newDescription = "Enjoy Thanksgiving Day";
        LocalDate newDate = LocalDate.of(2024,11,29);
        Location newlocation = new Location();
        newlocation.setId(2);
        Double newcost = 78.0;
        Integer newduration = 1;
        String newimage = "Image 1";
        when(locationDao.selectLocationById(2)).thenReturn(Optional.of(newlocation));

        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(newEvent,newDescription,newimage,newDate,newcost,newduration,newlocation.getId());
        lenient().when(eventDao.existsEventByName(newEvent)).thenReturn(false);
        underTest.updateEvent(id, eventUpdateRequest);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        verify(eventDao).updateEvent(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent.getName()).isEqualTo(eventUpdateRequest.name());
        assertThat(capturedEvent.getDescription()).isEqualTo(eventUpdateRequest.description());
        assertThat(capturedEvent.getImage()).isEqualTo(eventUpdateRequest.image());
        assertThat(capturedEvent.getDuration()).isEqualTo(eventUpdateRequest.duration());
        assertThat(capturedEvent.getDate()).isEqualTo(eventUpdateRequest.date());
        assertThat(capturedEvent.getLocation().getId()).isEqualTo(eventUpdateRequest.location_id());
        assertThat(capturedEvent.getCost()).isEqualTo(eventUpdateRequest.cost());


    }
}
