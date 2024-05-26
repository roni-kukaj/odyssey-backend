package com.odyssey.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String description;
    @Column(nullable = false) private Integer duration;
    @Column(nullable = false) private Integer cost;

    @ManyToOne()
    @JoinColumn(name="location_id", referencedColumnName = "id")
    private Location location;

    public Activity(){}

    public Activity(String name, String description, Integer cost, Integer duration, Location location) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
    }

    public Activity(Integer id, String name, String description, int cost, int duration, Location location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getDuration() {
        return duration;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(name, activity.name) && Objects.equals(description, activity.description) && Objects.equals(cost, activity.cost) && Objects.equals(duration, activity.duration) && Objects.equals(location, activity.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, cost, duration, location);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", duration=" + duration +
                ", location=" + location +
                '}';
    }
}
