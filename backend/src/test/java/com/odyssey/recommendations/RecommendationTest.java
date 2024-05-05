package com.odyssey.recommendations;

import com.odyssey.activities.Activity;
import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationTest {

    @Test
    void testRecommendationGettersAndSetters() {
        Recommendation recommendation = new Recommendation();
        User user = new User();
        Activity activity = new Activity();

        recommendation.setDescription("A must - vist destination for any traveler seeking unforgettable memories");
        recommendation.setUser(user);
        recommendation.setActivity(activity);

        assertEquals("A must - vist destination for any traveler seeking unforgettable memories", recommendation.getDescription());
        assertEquals(user,recommendation.getUser());
        assertEquals(activity,recommendation.getActivity());


    }
}
