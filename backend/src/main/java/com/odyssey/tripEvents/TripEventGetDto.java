package com.odyssey.tripEvents;

import com.odyssey.events.Event;
import com.odyssey.tripActivities.TripActivityGetDto;

import java.time.LocalDate;
import java.util.Objects;

public class TripEventGetDto {
    private Integer id;
    private Event event;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripEventGetDto() {}

    public TripEventGetDto(Event event, LocalDate plannedDate, Integer visitOrder) {
        this.event = event;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripEventGetDto(Integer id, Event event, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
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
        TripEventGetDto that = (TripEventGetDto) o;
        return Objects.equals(id, that.id) && Objects.equals(event, that.event) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripEventGetDto{" +
                "id=" + id +
                ", event=" + event +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
