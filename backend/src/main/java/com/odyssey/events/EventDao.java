package com.odyssey.events;

import java.util.List;
import java.util.Optional;

public interface EventDao {
    List<Event> selectAllEvents();
    Optional<Event> selectEventById(Integer id);
    void insertEvent(Event event);
    void updateEvent(Event event);
    boolean existsEventById(Integer id);
    boolean existsEventByName(String name);
    void deleteEventById(Integer id);

}

