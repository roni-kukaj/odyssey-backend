package com.odyssey.tripPlaces;

import com.odyssey.locations.Location;

import java.time.LocalDate;
import java.util.Objects;

public class TripPlaceGetDto {
    private Integer id;
    private Location location;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripPlaceGetDto() {}

    public TripPlaceGetDto(Location location, LocalDate plannedDate, Integer visitOrder) {
        this.location = location;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripPlaceGetDto(Integer id, Location location, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
        this.location = location;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
        TripPlaceGetDto that = (TripPlaceGetDto) o;
        return Objects.equals(id, that.id) && Objects.equals(location, that.location) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripPlaceGetDto{" +
                "id=" + id +
                ", location=" + location +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
