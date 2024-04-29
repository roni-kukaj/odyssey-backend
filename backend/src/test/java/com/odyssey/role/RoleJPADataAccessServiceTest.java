package com.odyssey.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class RoleJPADataAccessServiceTest {
    private RoleJPADataAccessService roleDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        roleDataAccessService = new RoleJPADataAccessService(roleRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllRoles() {
        roleDataAccessService.selectAllRoles();
        verify(roleRepository).findAll();
    }

    @Test
    void selectRoleById() {
        int id = 1;
        roleDataAccessService.selectRoleById(id);
        verify(roleRepository).findById(id);
    }

    @Test
    void insertRole() {
        Role role = new Role(1, "user");
        roleDataAccessService.insertRole(role);
        verify(roleRepository).save(role);
    }

    @Test
    void existsRoleById() {
        int id = 1;
        roleDataAccessService.existsRoleById(id);
        verify(roleRepository).existsRoleById(id);
    }

    @Test
    void existsRoleByName() {
        String name = "user";
        roleDataAccessService.existsRoleByName(name);
        verify(roleRepository).existsRoleByName(name);
    }

    @Test
    void updateRole() {
        Role role = new Role(1, "useri");
        roleDataAccessService.updateRole(role);
        verify(roleRepository).save(role);
    }

    @Test
    void deleteRoleById() {
        int id = 1;
        roleDataAccessService.deleteRoleById(id);
        verify(roleRepository).deleteById(id);
    }
}