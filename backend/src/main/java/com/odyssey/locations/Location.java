package com.odyssey.locations;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String picture;

    public Location() {}

    public Location(String city, String country, String picture) {
        this.city = city;
        this.country = country;
        this.picture = picture;
    }

    public Location(Integer id, String city, String country, String picture) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) &&
                Objects.equals(city, location.city) &&
                Objects.equals(country, location.country) &&
                Objects.equals(picture, location.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, country, picture);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
