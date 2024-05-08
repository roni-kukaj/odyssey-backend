package com.odyssey.tripPlaces;

import java.time.LocalDate;
import java.util.Objects;

public class TripPlacePostDto {
    private Integer locationId;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripPlacePostDto() {}

    public TripPlacePostDto(Integer locationId, LocalDate plannedDate, Integer visitOrder) {
        this.locationId = locationId;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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
        TripPlacePostDto that = (TripPlacePostDto) o;
        return Objects.equals(locationId, that.locationId) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripPlacePostDto{" +
                "locationId=" + locationId +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
