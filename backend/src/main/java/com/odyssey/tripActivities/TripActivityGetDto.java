package com.odyssey.tripActivities;

import com.odyssey.activities.Activity;

import java.time.LocalDate;
import java.util.Objects;

public class TripActivityGetDto {
    private Integer id;
    private Activity activity;
    private LocalDate plannedDate;
    private Integer visitOrder;

    public TripActivityGetDto() {}

    public TripActivityGetDto(Activity activity, LocalDate plannedDate, Integer visitOrder) {
        this.activity = activity;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripActivityGetDto(Integer id, Activity activity, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
        this.activity = activity;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
        TripActivityGetDto that = (TripActivityGetDto) o;
        return Objects.equals(id, that.id) && Objects.equals(activity, that.activity) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activity, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripActivityGetDto{" +
                "id=" + id +
                ", activity=" + activity +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
