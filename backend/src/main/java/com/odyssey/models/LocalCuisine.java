package com.odyssey.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="local_cuisine")
public class LocalCuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String description;
    @Column(nullable = false) private String image;

    @ManyToOne()
    @JoinColumn(name="location_id", referencedColumnName = "id")
    private Location location;

    public LocalCuisine(){}

    public LocalCuisine(Integer id, String name, String description, String image, Location location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    public LocalCuisine(String name, String description, String image, Location location) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalCuisine that = (LocalCuisine) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(image, that.image) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(image);
        result = 31 * result + Objects.hashCode(location);
        return result;
    }

    @Override
    public String toString() {
        return "localCuisine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", location=" + location +
                '}';
    }
}
