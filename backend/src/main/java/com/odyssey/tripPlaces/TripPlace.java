package com.odyssey.tripPlaces;

import com.odyssey.events.Event;
import com.odyssey.locations.Location;
import com.odyssey.trips.Trip;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trip_place_list")
public class TripPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(nullable = false, name = "planned_date") private LocalDate plannedDate;
    @Column(nullable = false, name = "visit_order") private Integer visitOrder;

    public TripPlace() {}

    public TripPlace(Trip trip, Location location, LocalDate plannedDate, Integer visitOrder) {
        this.trip = trip;
        this.location = location;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripPlace(Integer id, Trip trip, Location location, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
        this.trip = trip;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
        TripPlace tripPlace = (TripPlace) o;
        return Objects.equals(id, tripPlace.id) && Objects.equals(trip, tripPlace.trip) && Objects.equals(location, tripPlace.location) && Objects.equals(plannedDate, tripPlace.plannedDate) && Objects.equals(visitOrder, tripPlace.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, location, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripPlace{" +
                "id=" + id +
                ", trip=" + trip +
                ", location=" + location +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
