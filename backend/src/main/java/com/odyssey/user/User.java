package com.odyssey.user;

import jakarta.persistence.*;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "user_username_unique",
                        columnNames = "username"
                )
        }
)
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    private Integer id;
    @Column(nullable = false) private String fullName;
    @Column(nullable = false) private String username;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String location;


    public User(int fullName, String username, String email, String password, String location) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public User(Integer id, String fullName, String username, String email, String password, String location) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
