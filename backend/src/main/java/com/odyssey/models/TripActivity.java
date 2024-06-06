package com.odyssey.models;

import jakarta.persistence.*;

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

    public TripActivity() {}

    public TripActivity(Trip trip, Activity activity) {
        this.trip = trip;
        this.activity = activity;
    }

    public TripActivity(Integer id, Trip trip, Activity activity) {
        this.id = id;
        this.trip = trip;
        this.activity = activity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripActivity that = (TripActivity) o;
        return Objects.equals(id, that.id) && Objects.equals(trip, that.trip) && Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trip, activity);
    }

    @Override
    public String toString() {
        return "TripActivity{" +
                "id=" + id +
                ", trip=" + trip +
                ", activity=" + activity +
                '}';
    }
}
