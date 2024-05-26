package com.odyssey.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, name = "start_date") private LocalDate startDate;
    @Column(nullable = false, name = "end_date") private LocalDate endDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "trip_items",
            joinColumns = {
                    @JoinColumn(name = "trip_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id", referencedColumnName = "id")
            }
    )
    private Set<Item> items;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "trip_place_list",
        joinColumns = {
            @JoinColumn(name = "trip_id", referencedColumnName = "id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "location_id", referencedColumnName = "id")
        }
    )
    private Set<Location> places;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "trip_activity_list",
            joinColumns = {
                    @JoinColumn(name = "trip_id", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "activity_id", referencedColumnName = "id")
    }
    )
    private Set<Activity> activities;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "trip_event_list",
            joinColumns = {
                    @JoinColumn(name = "trip_id", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "event_id", referencedColumnName = "id")
    }
    )
    private Set<Event> events;

    public Trip() {}

    public Trip(User user, LocalDate startDate, LocalDate endDate, Set<Item> item, Set<Location> places, Set<Activity> activities, Set<Event> events) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = item;
        this.places = places;
        this.activities = activities;
        this.events = events;
    }

    public Trip(Integer id, User user, LocalDate startDate, LocalDate endDate, Set<Item> item, Set<Location> places, Set<Activity> activities, Set<Event> events) {
        this.id = id;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = item;
        this.places = places;
        this.activities = activities;
        this.events = events;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Location> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Location> places) {
        this.places = places;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) && Objects.equals(user, trip.user) && Objects.equals(startDate, trip.startDate) && Objects.equals(endDate, trip.endDate) && Objects.equals(items, trip.items) && Objects.equals(places, trip.places) && Objects.equals(activities, trip.activities) && Objects.equals(events, trip.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, startDate, endDate, items, places, activities, events);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", item=" + items +
                ", places=" + places +
                ", activities=" + activities +
                ", events=" + events +
                '}';
    }
}
