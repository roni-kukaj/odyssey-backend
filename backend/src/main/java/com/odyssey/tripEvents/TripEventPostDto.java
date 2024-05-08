package com.odyssey.tripEvents;

import java.time.LocalDate;
import java.util.Objects;

public class TripEventPostDto {
    private Integer eventId;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripEventPostDto() {}

    public TripEventPostDto(Integer eventId, LocalDate plannedDate, Integer visitOrder) {
        this.eventId = eventId;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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
        TripEventPostDto that = (TripEventPostDto) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripEventPostDto{" +
                "eventId=" + eventId +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
