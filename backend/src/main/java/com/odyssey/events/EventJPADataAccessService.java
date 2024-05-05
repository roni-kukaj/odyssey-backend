package com.odyssey.events;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("eventJPAService")
public class EventJPADataAccessService implements EventDao {

    private final EventRepository eventRepository;

    public EventJPADataAccessService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> selectAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> selectEventById(Integer id) {
        return eventRepository.findById(id);
    }

    @Override
    public void insertEvent(Event event) {
        eventRepository.save(event);

    }

    @Override
    public void updateEvent(Event event) {
        eventRepository.save(event);

    }

    @Override
    public boolean existsEventById(Integer id) {
        return eventRepository.existsEventById(id);
    }

    @Override
    public boolean existsEventByName(String name) {
        return eventRepository.existsEventByName(name);
    }

    @Override
    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);

    }
}
