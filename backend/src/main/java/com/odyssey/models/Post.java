package com.odyssey.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "text") private String text;
    @Column(nullable = false, name = "image") private String image;
    @Column(nullable = false, name = "posted_time") private LocalDate postedTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    public Post() {}

    public Post(String text, String image, LocalDate postedTime, User user, Trip trip) {
        this.text = text;
        this.image = image;
        this.postedTime = postedTime;
        this.user = user;
        this.trip = trip;
    }

    public Post(Integer id, String text, String image, LocalDate postedTime, User user, Trip trip) {
        this.id = id;
        this.text = text;
        this.image = image;
        this.postedTime = postedTime;
        this.user = user;
        this.trip = trip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalDate postedTime) {
        this.postedTime = postedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(text, post.text) && Objects.equals(image, post.image) && Objects.equals(postedTime, post.postedTime) && Objects.equals(user, post.user) && Objects.equals(trip, post.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, image, postedTime, user, trip);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", postedTime=" + postedTime +
                ", user=" + user +
                ", trip=" + trip +
                '}';
    }
}
