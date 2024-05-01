package com.odyssey.activities;

import com.odyssey.locations.Location;
import com.odyssey.role.Role;
import jakarta.persistence.*;

import java.util.Objects;

public class Activity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String description;
    @Column(nullable = false) private Integer cost;
    @Column(nullable = false) private Integer duration;

    @ManyToOne()
    @JoinColumn(name="locationId", referencedColumnName = "id")
    private Location location;

    public Activity(){}

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

        if (cost != activity.cost) return false;
        if (duration != activity.duration) return false;
        if (!Objects.equals(id, activity.id)) return false;
        if (!Objects.equals(name, activity.name)) return false;
        if (!Objects.equals(description, activity.description))
            return false;
        return Objects.equals(location, activity.location);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + duration;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
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
