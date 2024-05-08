package com.odyssey.tripActivities;

import com.odyssey.trips.Trip;
import com.odyssey.activities.Activity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trip_activity_list")
public class TripActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    @Column(nullable = false, name = "planned_date") private LocalDate plannedDate;
    @Column(nullable = false, name = "visit_order") private Integer visitOrder;

    public TripActivity() {}

    public TripActivity(Trip trip, Activity activity, LocalDate plannedDate, Integer visitOrder) {
        this.trip = trip;
        this.activity = activity;
        this.plannedDate = plannedDate;
        this.visitOrder = visitOrder;
    }

    public TripActivity(Integer id, Trip trip, Activity activity, LocalDate plannedDate, Integer visitOrder) {
        this.id = id;
        this.trip = trip;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
        TripActivity that = (TripActivity) o;
        return Objects.equals(id, that.id) && Objects.equals(trip, that.trip) && Objects.equals(activity, that.activity) && Objects.equals(plannedDate, that.plannedDate) && Objects.equals(visitOrder, that.visitOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, activity, plannedDate, visitOrder);
    }

    @Override
    public String toString() {
        return "TripActivity{" +
                "id=" + id +
                ", trip=" + trip +
                ", activity=" + activity +
                ", plannedDate=" + plannedDate +
                ", visitOrder=" + visitOrder +
                '}';
    }
}
