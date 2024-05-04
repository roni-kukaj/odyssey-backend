package com.odyssey.localCuisine;

import com.odyssey.locations.Location;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class LocalCuisineTest {

    @Test
    void testLocalCuisineGettersAndSetters() {
        LocalCuisine localCuisine = new LocalCuisine();
        Location l = new Location();
        localCuisine.setId(1);
        localCuisine.setName("Emrii");
        localCuisine.setDescription("We have...");
        localCuisine.setImage("pic.jpg");
        localCuisine.setLocation(l);

        assertEquals(1, localCuisine.getId());
        assertEquals("Emrii", localCuisine.getName());
        assertEquals("We have...", localCuisine.getDescription());
        assertEquals("pic.jpg", localCuisine.getImage());
        assertEquals(l, localCuisine.getLocation());

    }
}
