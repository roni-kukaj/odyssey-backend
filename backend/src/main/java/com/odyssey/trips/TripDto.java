package com.odyssey.trips;

import com.odyssey.tripActivities.TripActivity;
import com.odyssey.tripEvents.TripEvent;
import com.odyssey.tripItems.TripItem;
import com.odyssey.tripPlaces.TripPlace;
import com.odyssey.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TripDto {
    private Integer id;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TripItem> tripItems;
    private List<TripPlace> tripPlaces;
    private List<TripActivity> tripActivities;
    private List<TripEvent> tripEvents;

    public TripDto() {}

    public TripDto(Integer id, User user, LocalDate startDate, LocalDate endDate, List<TripItem> tripItems, List<TripPlace> tripPlaces, List<TripActivity> tripActivities, List<TripEvent> tripEvents) {
        this.id = id;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripItems = tripItems;
        this.tripPlaces = tripPlaces;
        this.tripActivities = tripActivities;
        this.tripEvents = tripEvents;
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

    public List<TripItem> getTripItems() {
        return tripItems;
    }

    public void setTripItems(List<TripItem> tripItems) {
        this.tripItems = tripItems;
    }

    public List<TripPlace> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(List<TripPlace> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }

    public List<TripActivity> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(List<TripActivity> tripActivities) {
        this.tripActivities = tripActivities;
    }

    public List<TripEvent> getTripEvents() {
        return tripEvents;
    }

    public void setTripEvents(List<TripEvent> tripEvents) {
        this.tripEvents = tripEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDto tripDto = (TripDto) o;
        return Objects.equals(id, tripDto.id) && Objects.equals(user, tripDto.user) && Objects.equals(startDate, tripDto.startDate) && Objects.equals(endDate, tripDto.endDate) && Objects.equals(tripItems, tripDto.tripItems) && Objects.equals(tripPlaces, tripDto.tripPlaces) && Objects.equals(tripActivities, tripDto.tripActivities) && Objects.equals(tripEvents, tripDto.tripEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, startDate, endDate, tripItems, tripPlaces, tripActivities, tripEvents);
    }

    @Override
    public String toString() {
        return "TripDto{" +
                "id=" + id +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", tripItems=" + tripItems +
                ", tripPlaces=" + tripPlaces +
                ", tripActivities=" + tripActivities +
                ", tripEvents=" + tripEvents +
                '}';
    }
}
