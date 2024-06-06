package com.odyssey.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String email;

    public Subscriber(){}

    public Subscriber(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Subscriber(String email){
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscriber that = (Subscriber) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(email);
        return result;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
