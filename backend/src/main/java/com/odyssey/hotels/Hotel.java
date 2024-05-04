package com.odyssey.hotels;

import com.odyssey.locations.Location;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.util.Objects;

@Entity
@Table(name="hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String name;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @Column(nullable = false) private Double rating;
    @Column(nullable = false, name="booking_link") private String bookingLink;

    public Hotel() {

    }

    public Hotel(String name, Location location, Double rating, String bookingLink) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.bookingLink = bookingLink;
    }

    public Hotel(Integer id, String name, Location location, Double rating, String bookingLink) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.bookingLink = bookingLink;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getBookingLink() {
        return bookingLink;
    }

    public void setBookingLink(String bookingLink) {
        this.bookingLink = bookingLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) && Objects.equals(name, hotel.name) && Objects.equals(location, hotel.location) && Objects.equals(rating, hotel.rating) && Objects.equals(bookingLink, hotel.bookingLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, rating, bookingLink);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", rating=" + rating +
                ", booking_link='" + bookingLink + '\'' +
                '}';
    }
}
