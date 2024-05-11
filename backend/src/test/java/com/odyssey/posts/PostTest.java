package com.odyssey.posts;

import com.odyssey.trips.Trip;
import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class PostTest {
    @Test
    void testPostGettersAndSetters() {
        User user = new User();
        Trip trip = new Trip();
        LocalDate date = LocalDate.now();
        Post post = new Post();
        post.setId(1);
        post.setText("A");
        post.setImage("A");
        post.setPostedTime(date);
        post.setUser(user);
        post.setTrip(trip);


        assertEquals(1, post.getId());
        assertEquals("A", post.getText());
        assertEquals("A", post.getImage());
        assertEquals(date, post.getPostedTime());
        assertEquals(user, post.getUser());
        assertEquals(trip, post.getTrip());

    }
}
