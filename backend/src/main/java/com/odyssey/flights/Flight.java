package com.odyssey.flights;

import com.odyssey.locations.Location;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name="flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private Timestamp time;

    @ManyToOne()
    @JoinColumn(name="origin_id", referencedColumnName = "id")
    private Location origin;

    @ManyToOne()
    @JoinColumn(name="destination_id", referencedColumnName = "id")
    private Location destination;

    public Flight(){}

    public Flight(String name, Timestamp time,  Location origin, Location destination) {
        this.name = name;
        this.time = time;
        this.origin = origin;
        this.destination = destination;
    }

    public Flight(Integer id, String name, Timestamp time,  Location origin, Location destination) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.origin = origin;
        this.destination = destination;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) && Objects.equals(name, flight.name) && Objects.equals(time, flight.time) && Objects.equals(origin, flight.origin) && Objects.equals(destination, flight.destination);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(time);
        result = 31 * result + Objects.hashCode(origin);
        result = 31 * result + Objects.hashCode(destination);
        return result;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", origin=" + origin +
                ", destination=" + destination +
                '}';
    }
}
