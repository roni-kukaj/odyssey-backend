package com.odyssey.role;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock private RoleDao roleDao;
    private RoleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoleService(roleDao);
    }

    @Test
    void getAllRoles() {
        // When
        underTest.getAllRoles();

        // Then
        verify(roleDao).selectAllRoles();
    }

    @Test
    void canGetRole() {
        // Given
        int id = 1;
        Role role = new Role(1, "user");

        when(roleDao.selectRoleById(id)).thenReturn(Optional.of(role));

        // When
        Role actual = underTest.getRole(id);

        // Then
        assertThat(actual).isEqualTo(role);
    }

    @Test
    void willThrowWhenGetRoleReturnEmptyOptional() {
        // Given
        int id = 1;
        when(roleDao.selectRoleById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getRole(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("role with id [%s] not found".formatted(id));
    }

    @Test
    void addRole() {
        // Given
        String name = "user";
        when(roleDao.existsRoleByName(name)).thenReturn(false);

        RoleRegistrationRequest request = new RoleRegistrationRequest(name);

        // When
        underTest.addRole(request);

        // Then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleDao).insertRole(roleArgumentCaptor.capture());

        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole.getId()).isNull();
        assertThat(capturedRole.getName()).isEqualTo(request.name());
    }

    @Test
    void willThrowWhenNameExistsWhileAddingRole() {
        // Given
        String name = "user";

        when(roleDao.existsRoleByName(name)).thenReturn(true);

        RoleRegistrationRequest request = new RoleRegistrationRequest(name);

        // When
        assertThatThrownBy(() -> underTest.addRole(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("role already exists");

        // Then
        verify(roleDao, never()).insertRole(any());
    }

    @Test
    void willThrowDeleteRoleByIdNotExists() {
        // Given
        int id = 10;
        when(roleDao.existsRoleById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteRole(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("role with id [%s] not found".formatted(id));

        // Then
        verify(roleDao, never()).deleteRoleById(any());
    }

    @Test
    void canUpdateAllRoleProperties() {
        // Given
        int id = 1;
        Role role = new Role(
                id,
                "user"
        );

        when(roleDao.selectRoleById(id)).thenReturn(Optional.of(role));

        String newName = "admin";
        RoleUpdateRequest request = new RoleUpdateRequest(newName);
        lenient().when(roleDao.existsRoleByName(newName)).thenReturn(false);

        // When
        underTest.updateRole(id, request);

        // Then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleDao).updateRole(roleArgumentCaptor.capture());
        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole.getName()).isEqualTo(request.name());
    }

}