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

    @Column(nullable = false, name = "planned_date") private LocalDate plannedDate;
    @Column(nullable = false, name = "visit_order") private Integer visitOrder;

    public TripEvent() {}

    public TripEvent(Trip trip, Event event, LocalDate plannedDate, Integer visitOrder) {
        this.trip = trip;
        this.event = event;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripEvent(Integer id, Trip trip, Event event, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
        this.trip = trip;
        this.event = event;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
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

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public Integer getVisitOrder() {
        return visitOrder;
    }

    public void setVisitOrder(Integer visitOrder) {
        this.visitOrder = visitOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEvent tripEvent = (TripEvent) o;
        return Objects.equals(id, tripEvent.id) && Objects.equals(trip, tripEvent.trip) && Objects.equals(event, tripEvent.event) && Objects.equals(plannedDate, tripEvent.plannedDate) && Objects.equals(visitOrder, tripEvent.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, event, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripEvent{" +
                "id=" + id +
                ", trip=" + trip +
                ", event=" + event +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
