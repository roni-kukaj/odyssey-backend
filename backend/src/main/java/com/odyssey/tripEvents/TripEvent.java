package com.odyssey.tripEvents;


import com.odyssey.activities.Activity;
import com.odyssey.events.Event;
import com.odyssey.trips.Trip;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trip_event_list")
public class TripEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    public TripEvent() {}

    public TripEvent(Trip trip, Event event) {
        this.trip = trip;
        this.event = event;
    }

    public TripEvent(Integer id, Trip trip, Event event) {
        this.id = id;
        this.trip = trip;
        this.event = event;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEvent tripEvent = (TripEvent) o;
        return Objects.equals(id, tripEvent.id) && Objects.equals(trip, tripEvent.trip) && Objects.equals(event, tripEvent.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, event);
    }

    @Override
    public String toString() {
        return "TripEvent{" +
                "id=" + id +
                ", trip=" + trip +
                ", event=" + event +
                '}';
    }
}
