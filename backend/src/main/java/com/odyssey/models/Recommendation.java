package com.odyssey.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) private String description;
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name="activity_id",referencedColumnName = "id")
    private Activity activity;

    public Recommendation(){}

    public Recommendation(Integer id, String description,User user, Activity activity){
        this.id = id;
        this.description = description;
        this.user = user;
        this.activity = activity;
    }

    public Recommendation(String description,User user, Activity activity){
        this.description = description;
        this.user = user;
        this.activity = activity;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        Recommendation recommendation = (Recommendation)o;
        return Objects.equals(id,recommendation.id)&&Objects.equals(description,recommendation.description)
                && Objects.equals(user,recommendation.user) && Objects.equals(activity,recommendation.activity);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,description,user,activity);
    }

    @Override
    public String toString(){
        return "Recommendation{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", activity=" + activity +
                '}';
    }
}
