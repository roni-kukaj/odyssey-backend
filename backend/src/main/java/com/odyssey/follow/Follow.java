package com.odyssey.follow;

import com.odyssey.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "followers")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name="follower_id")
    private User follower;

    @ManyToOne()
    @JoinColumn(name="following_id")
    private User following;

    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Follow(Integer id, User follower, User following) {
        this.id = id;
        this.follower = follower;
        this.following = following;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(id, follow.id) && Objects.equals(follower, follow.follower) && Objects.equals(following, follow.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, follower, following);
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + follower +
                ", following=" + following +
                '}';
    }
}
