package com.odyssey.user;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDao);
    }

    @Test
    void getAllUsers() {
        // When
        underTest.getAllUsers();

        // Then
        verify(userDao).selectAllUsers();
    }

    @Test
    void canGetUser() {
        // Given
        int id = 1;
        User user = new User(
                id,
                "Filan Fisteku",
                "filanfisteku",
                "filanfisteku@gmail.com",
                "passi",
                1,
                "avatar1"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        // When
        User actual = underTest.getUser(id);

        // Then
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void willThrowWhenGetUserReturnEmptyOptional() {
        // Given
        int id = 1;
        when(userDao.selectUserById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getUser(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

    }

    @Test
    void addUser() {
        // Given
        String email = "filanfisteku@gmail.com";
        when(userDao.existsUserByEmail(email)).thenReturn(false);

        UserRegistrationRequest request = new UserRegistrationRequest(
                "Filan Fisteku",
                "filanfisteku",
                "filanfisteku@gmail.com",
                "passi",
                1,
                "avatar1"
        );

        // When
        underTest.addUser(request);

        // Then
        ArgumentCaptor<User> customerArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).insertUser(customerArgumentCaptor.capture());

        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isNull();
        assertThat(capturedUser.getFullName()).isEqualTo(request.fullname());
        assertThat(capturedUser.getUsername()).isEqualTo(request.username());
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(request.password());
        assertThat(capturedUser.getLocation()).isEqualTo(request.location());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingAUser() {
        // Given
        String email = "filani@gmail.com";

        when(userDao.existsUserByEmail(email)).thenReturn(true);

        UserRegistrationRequest request = new UserRegistrationRequest(
                "Filan Fisteku",
                "filanfisteku",
                "filani@gmail.com",
                "passi",
                1,
                "avatar1"
        );

        // When
        assertThatThrownBy(() -> underTest.addUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(userDao, never()).insertUser(any());
    }

    @Test
    void willThrowWhenUsernameExistsWhileAddingAUser() {
        // Given
        String username = "filanfisteku";

        when(userDao.existsUserByUsername(username)).thenReturn(true);

        UserRegistrationRequest request = new UserRegistrationRequest(
                "Filan Fisteku",
                "filanfisteku",
                "filanfisteku@gmail.com",
                "passi",
                1,
                "avatar1"
        );

        // When
        assertThatThrownBy(() -> underTest.addUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("username already taken");

        // Then
        verify(userDao, never()).insertUser(any());
    }

    @Test
    void deleteUserById() {
        // Given
        int id = 10;
        when(userDao.existsUserById(id)).thenReturn(true);

        // When
        underTest.deleteUser(id);

        // Then
        verify(userDao).deleteUserById(id);
    }

    @Test
    void willThrowDeleteUserByIdNotExists() {
        // Given
        int id = 10;

        when(userDao.existsUserById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

        // Then
        verify(userDao, never()).deleteUserById(any());
    }

    @Test
    void canUpdateAllUsersProperties() {
        // Given
        int id = 10;
        User user = new User(
                id,
                "Filan Fisteku",
                "filanfisteku",
                "filanfisteku@gmail.com",
                "passi",
                1,
                "avatar1"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        String newEmail = "fisteku@gmail.com";
        String newUsername = "ff";
        UserUpdateRequest request = new UserUpdateRequest(
                "FF",
                newUsername,
                newEmail,
                "password",
                1,
                "avatar1"
        );

        when(userDao.existsUserByEmail(newEmail)).thenReturn(false);
        when(userDao.existsUserByUsername(newUsername)).thenReturn(false);

        // When
        underTest.updateUser(id, request);

        // Then
        ArgumentCaptor<User> customerArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userDao).updateUser(customerArgumentCaptor.capture());
        User capturedUser = customerArgumentCaptor.getValue();

        assertThat(capturedUser.getFullName()).isEqualTo(request.fullname());
        assertThat(capturedUser.getUsername()).isEqualTo(request.username());
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(request.password());
        assertThat(capturedUser.getLocation()).isEqualTo(request.location());
    }

}