package com.odyssey.trips;

import com.odyssey.tripActivities.TripActivityGetDto;
import com.odyssey.tripEvents.TripEventGetDto;
import com.odyssey.tripItems.TripItemGetDto;
import com.odyssey.tripPlaces.TripPlaceGetDto;
import com.odyssey.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TripGetDto {
    private Integer id;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TripItemGetDto> tripItems;
    private List<TripPlaceGetDto> tripPlaces;
    private List<TripActivityGetDto> tripActivities;
    private List<TripEventGetDto> tripEvents;

    public TripGetDto() {}

    public TripGetDto(Integer id, User user, LocalDate startDate, LocalDate endDate, List<TripItemGetDto> tripItems, List<TripPlaceGetDto> tripPlaces, List<TripActivityGetDto> tripActivities, List<TripEventGetDto> tripEvents) {
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

    public List<TripItemGetDto> getTripItems() {
        return tripItems;
    }

    public void setTripItems(List<TripItemGetDto> tripItems) {
        this.tripItems = tripItems;
    }

    public List<TripPlaceGetDto> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(List<TripPlaceGetDto> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }

    public List<TripActivityGetDto> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(List<TripActivityGetDto> tripActivities) {
        this.tripActivities = tripActivities;
    }

    public List<TripEventGetDto> getTripEvents() {
        return tripEvents;
    }

    public void setTripEvents(List<TripEventGetDto> tripEvents) {
        this.tripEvents = tripEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripGetDto tripDto = (TripGetDto) o;
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
