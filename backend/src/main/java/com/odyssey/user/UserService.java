package com.odyssey.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.exception.*;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(@Qualifier("jpa") UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUser(Integer id) {
        return userDao.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(id)));
    }

    public void addUser(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.email();
        if (userDao.existsUserByEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        String username = userRegistrationRequest.username();
        if (userDao.existsUserByUsername(username)) {
            throw new DuplicateResourceException("username already taken");
        }

        User user = new User(
                userRegistrationRequest.fullname(),
                userRegistrationRequest.username(),
                userRegistrationRequest.email(),
                userRegistrationRequest.password(),
                userRegistrationRequest.location()
        );

        userDao.insertUser(user);
    }

    public void deleteUser(Integer id) {
        if (userDao.existsUserById(id)) {
            userDao.deleteUserById(id);
        }
        else {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(id));
        }
    }

    public void updateUser(Integer id, UserUpdateRequest updateRequest) {
        User user = getUser(id);
        boolean changes = false;

        if (updateRequest.fullname() != null && !updateRequest.fullname().equals(user.getFullName())) {
            user.setFullName(updateRequest.fullname());
            changes = true;
        }
        if (updateRequest.username() != null && !updateRequest.username().equals(user.getUsername())) {
            if (userDao.existsUserByUsername(updateRequest.username())) {
                throw new DuplicateResourceException("username already taken");
            }
            user.setUsername(updateRequest.username());
            changes = true;
        }
        if (updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())) {
            if (userDao.existsUserByEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            user.setEmail(updateRequest.email());
            changes = true;
        }
        if (updateRequest.password() != null && !updateRequest.password().equals(user.getPassword())) {
            user.setPassword(updateRequest.password());
            changes = true;
        }
        if (updateRequest.location() != null && !updateRequest.location().equals(user.getLocation())) {
            user.setLocation(updateRequest.location());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }
        userDao.updateUser(user);
    }

}
