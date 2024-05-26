package com.odyssey.repositories;

import com.odyssey.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

    boolean existsEventById(Integer id);
    boolean existsEventByName(String name);
}
