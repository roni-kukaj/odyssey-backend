package com.odyssey.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "trip_items")
public class TripItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    public TripItem() {}

    public TripItem(Trip trip, Item item) {
        this.trip = trip;
        this.item = item;
    }

    public TripItem(Integer id, Trip trip, Item item) {
        this.id = id;
        this.trip = trip;
        this.item = item;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripItem tripItems = (TripItem) o;
        return Objects.equals(id, tripItems.id) && Objects.equals(trip, tripItems.trip) && Objects.equals(item, tripItems.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, item);
    }

    @Override
    public String toString() {
        return "TripItems{" +
                "id=" + id +
                ", trip=" + trip +
                ", item=" + item +
                '}';
    }
}
