package com.odyssey.plans;

import com.odyssey.locations.Location;
import com.odyssey.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @Column(nullable = false, name = "visit_date") private LocalDate visitDate;

    public Plan() {}

    public Plan(User user, Location location, LocalDate visitDate) {
        this.user = user;
        this.location = location;
        this.visitDate = visitDate;
    }

    public Plan(Integer id, User user, Location location, LocalDate visitDate) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.visitDate = visitDate;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return Objects.equals(id, plan.id) && Objects.equals(user, plan.user) && Objects.equals(location, plan.location) && Objects.equals(visitDate, plan.visitDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, location, visitDate);
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", user=" + user +
                ", location=" + location +
                ", visitDate=" + visitDate +
                '}';
    }
}
