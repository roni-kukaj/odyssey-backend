package com.odyssey.bookmarks;

import com.odyssey.locations.Location;
import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookmarksTest {

    @Test
    void testBookmarksGettersAndSetters() {
        Bookmarks bookmarks = new Bookmarks();
        bookmarks.setId(1);
        Location location = new Location();
        User user = new User();
        bookmarks.setLocation(location);
        bookmarks.setUser(user);

        assertEquals(1,bookmarks.getId());
        assertEquals(location,bookmarks.getLocation());
        assertEquals(user,bookmarks.getUser());
    }
}
