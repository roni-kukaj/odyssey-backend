package com.odysseybackend.backend;

import com.odyssey.User.User;
import com.odyssey.User.UserDataAccessService;
import com.odyssey.User.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserDataAccessServiceTest {
    private UserDataAccessService userDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userDataAccessService = new UserDataAccessService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void selectAllUsers() {
        userDataAccessService.selectAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void selectUserById() {

        int Id = 1;

        userDataAccessService.selectUserById(Id);
        verify(userRepository).findById(Id);
    }

    @Test
    void insertUser() {
        User user = new User(1,"Admin","admin","admin@gmail.com","admin","London");
        userDataAccessService.insertUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void existsUserByEmail() {

        String Email = "admin@gmail.com";

        userDataAccessService.existsUserByEmail(Email);
        verify(userRepository).existsUserByEmail(Email);

    }

    @Test
    void existsUserById() {

        int Id = 1;

        userDataAccessService.existsUserById(Id);
        verify(userRepository).existsUserById(Id);
    }

    @Test
    void existsUserByUsername() {

        String username = "admin";

        userDataAccessService.existsUserByUsername(username);
        verify(userRepository).existsUserByUsername(username);
    }
}