package com.odyssey.reviews;

import com.odyssey.models.Location;
import com.odyssey.models.Review;
import com.odyssey.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTest {

    @Test
    void testReviewGettersAndSetters() {
        Review review = new Review();
        User user = new User();
        Location location = new Location();

        review.setDescription("The best place to visit");
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);

        assertEquals("The best place to visit",review.getDescription());
        assertEquals(5,review.getRating());
        assertEquals(user,review.getUser());
        assertEquals(location,review.getLocation());
    }
}
