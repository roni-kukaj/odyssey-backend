package com.odyssey.follow;

import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowTest {

    @Test
    void testFollowGettersAndSetters() {
        User follower = new User();
        User following = new User();
        Follow follow = new Follow();

        follow.setId(1);
        follow.setFollower(follower);
        follow.setFollowing(following);

        assertEquals(1, follow.getId());
        assertEquals(follower, follow.getFollower());
        assertEquals(following, follow.getFollowing());
    }

}
