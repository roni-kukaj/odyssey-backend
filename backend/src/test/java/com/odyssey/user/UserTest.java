package com.odyssey.user;

import com.odyssey.role.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersAndSetters() {

        User user = new User();
        user.setId(1);
        user.setFullname("admin");
        user.setUsername("admin");
        user.setEmail("admin@mail.com");
        user.setPassword("password");
        user.setRole(new Role(2, "admin"));

        
        assertEquals(1, user.getId());
        assertEquals("admin", user.getFullname());
        assertEquals("admin", user.getUsername());
        assertEquals("admin@mail.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("admin", user.getRole().getName());
    }
}
