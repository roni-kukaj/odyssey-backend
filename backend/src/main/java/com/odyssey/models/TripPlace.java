package com.odyssey.models;

import jakarta.persistence.*;

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


    public TripPlace() {}

    public TripPlace(Trip trip, Location location) {
        this.trip = trip;
        this.location = location;
    }

    public TripPlace(Integer id, Trip trip, Location location) {
        this.id = id;
        this.trip = trip;
        this.location = location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripPlace tripPlace = (TripPlace) o;
        return Objects.equals(id, tripPlace.id) && Objects.equals(trip, tripPlace.trip) && Objects.equals(location, tripPlace.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, location);
    }

    @Override
    public String toString() {
        return "TripPlace{" +
                "id=" + id +
                ", trip=" + trip +
                ", location=" + location +
                '}';
    }
}
