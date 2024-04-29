package com.odyssey.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleGettersAndSetters() {
        Role role = new Role();
        role.setId(1);
        role.setName("Role 1");

        assertEquals(1, role.getId());
        assertEquals("Role 1", role.getName());
    }

}