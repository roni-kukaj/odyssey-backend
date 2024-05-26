package com.odyssey.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "bookmarks")
public class Bookmarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="location_id", referencedColumnName = "id")
    Location location;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    User user;


    public Bookmarks(){}

    public Bookmarks(Integer id, Location location, User user) {
        this.id = id;
        this.location = location;
        this.user = user;
    }

    public Bookmarks(Location location, User user) {
        this.location = location;
        this.user = user;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals (Object o){
        if(this == o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        Bookmarks bookmarks = (Bookmarks) o;
        return
                Objects.equals(id,bookmarks.id) && Objects.equals(location, bookmarks.location)
                        && Objects.equals(user,bookmarks.user);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,location,user);
    }

    @Override
    public String toString(){
        return "Bookmarks{" +
                "id=" + id +
                ", location=" + location +
                ", user=" + user +
                '}';
    }
}
