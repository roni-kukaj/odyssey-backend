package com.odyssey.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersAndSetters() {

        User user = new User();
        user.setId(1);
        user.setFullName("admin");
        user.setUsername("admin");
        user.setEmail("admin@mail.com");
        user.setPassword("password");
        user.setLocation("New York");

        
        assertEquals(1, user.getId());
        assertEquals("admin", user.getFullName());
        assertEquals("admin", user.getUsername());
        assertEquals("admin@mail.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("New York", user.getLocation());
    }
}
