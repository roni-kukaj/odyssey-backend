package com.odyssey.events;


import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.EventDao;
import com.odyssey.dtos.EventRegistrationDto;
import com.odyssey.dtos.EventUpdateDto;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Event;
import com.odyssey.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Mock
    private CloudinaryService cloudinaryService;

    private final String FILE_URL = "src/main/resources/images/test.png";
    private Path path;
    private byte[] content;

    private EventService underTest;

    @BeforeEach
    void setUp() throws IOException {
        underTest = new EventService(eventDao,locationDao, cloudinaryService);
        path = Paths.get(FILE_URL);
        content = Files.readAllBytes(path);
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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        when(eventDao.existsEventByName(name)).thenReturn(false);
        when(locationDao.selectLocationById(1)).thenReturn(Optional.of(location));

        EventRegistrationDto dto = new EventRegistrationDto (
                name, description, image, date, cost, duration, location.getId()
        );

        underTest.addEvent(dto);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventDao).insertEvent(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent.getId()).isNull();
        assertThat(capturedEvent.getName()).isEqualTo(dto.name());
        assertThat(capturedEvent.getDescription()).isEqualTo(dto.description());
        assertThat(capturedEvent.getImage()).isEqualTo(null);
        assertThat(capturedEvent.getDuration()).isEqualTo(dto.duration());
        assertThat(capturedEvent.getDate()).isEqualTo(dto.date());
        assertThat(capturedEvent.getLocation().getId()).isEqualTo(dto.locationId());
        assertThat(capturedEvent.getCost()).isEqualTo(dto.cost());

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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        when(eventDao.existsEventByName(name)).thenReturn(true);
        EventRegistrationDto dto = new EventRegistrationDto (
                name, description, image, date, cost, duration, location.getId()
        );
        assertThatThrownBy(()-> underTest.addEvent(dto)).isInstanceOf(DuplicateResourceException.class).hasMessage("event already exists");

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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        Event event = new Event(
                name, description, "pic", date, cost, duration, location
        );
        when(eventDao.selectEventById(id)).thenReturn(Optional.of(event));

        String newEvent = "Thanksgiving Day";
        String newDescription = "Enjoy Thanksgiving Day";
        LocalDate newDate = LocalDate.of(2024,11,29);
        Location newlocation = new Location();
        newlocation.setId(2);
        Double newCost = 78.0;
        Integer newDuration = 1;
        when(locationDao.selectLocationById(2)).thenReturn(Optional.of(newlocation));

        EventUpdateDto dto =
                new EventUpdateDto(
                        newEvent, newDescription, newDate, newCost, newDuration, newlocation.getId(), image
                );

        lenient().when(eventDao.existsEventByName(newEvent)).thenReturn(false);
        underTest.updateEventInformation(id, dto);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        verify(eventDao).updateEvent(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent.getName()).isEqualTo(dto.name());
        assertThat(capturedEvent.getDescription()).isEqualTo(dto.description());
        assertThat(capturedEvent.getImage()).isEqualTo(null);
        assertThat(capturedEvent.getDuration()).isEqualTo(dto.duration());
        assertThat(capturedEvent.getDate()).isEqualTo(dto.date());
        assertThat(capturedEvent.getLocation().getId()).isEqualTo(dto.locationId());
        assertThat(capturedEvent.getCost()).isEqualTo(dto.cost());
    }
}
