package com.odyssey.tripActivities;

import java.time.LocalDate;
import java.util.Objects;

public class TripActivityPostDto {
    private Integer activityId;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripActivityPostDto() {}

    public TripActivityPostDto(Integer activityId, LocalDate plannedDate, Integer visitOrder) {
        this.activityId = activityId;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
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
        TripActivityPostDto that = (TripActivityPostDto) o;
        return Objects.equals(activityId, that.activityId) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripActivityPostDto{" +
                "activityId=" + activityId +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
