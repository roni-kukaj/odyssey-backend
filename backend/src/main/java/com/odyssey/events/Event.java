package com.odyssey.events;

import com.odyssey.locations.Location;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="events")

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String description;
    @Column(nullable = false)private String image;
    @Column(nullable = false) private LocalDate date;
    @Column(nullable = false) private Double cost;
    @Column(nullable = false) private Integer duration;

    @ManyToOne()
    @JoinColumn(name="location_id", referencedColumnName = "id")
    private Location location;


    public Event(){}

    public Event(Integer id, String name, String description, String image, LocalDate date, Double cost, Integer duration,Location location_id){
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location_id;

    }

    public Event( String name, String description, String image, LocalDate date, Double cost, Integer duration,Location location_id){

        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location_id;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Location getLocation_id() {
        return location;
    }

    public void setLocation_id(Location location_id) {
        this.location = location_id;
    }


    @Override

    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null||getClass()!=o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id,event.id) && Objects.equals(name,event.name) &&
                Objects.equals(description,event.description) && Objects.equals(image,event.image) &&
                Objects.equals(date,event.date) && Objects.equals(cost,event.cost) &&
                Objects.equals(duration,event.duration) && Objects.equals(location,event.location);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,name,description,image,date,cost,duration,location);
    }

    @Override
    public String toString(){
        return "Event{" + "id=" + id + ", name='" + name +'\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", cost='" + cost + '\'' +
                ", duration=" + duration + '\'' +
                ", location_id='" + location + '\'' +
                '}';
    }
}
