package com.odyssey.reviews;

import com.odyssey.locations.Location;
import com.odyssey.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "reviews")

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) String description;
    @Column(nullable = false) Integer rating;

    @ManyToOne()
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name="location_id", referencedColumnName = "id")
    private Location location;

    public Review(){}

    public Review(Integer id, String description, Integer rating, User user, Location location) {
        this.id = id;
        this.description = description;
        this.rating = rating;
        this.user = user;
        this.location = location;
    }

    public Review(String description, Integer rating, User user, Location location) {
        this.description = description;
        this.rating = rating;
        this.user = user;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null||getClass()!=o.getClass()){
            return false;
        }

        Review review = (Review) o;
        return Objects.equals(id,review.id) && Objects.equals(description,review.description)
                && Objects.equals(user,review.user) && Objects.equals(location,review.location);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,description,user,location);
    }


    @Override
    public String toString(){
        return "Review{" + "id=" + id + ", description='" + description +'\'' +
                ", user='" + user + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
