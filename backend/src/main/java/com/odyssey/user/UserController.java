package com.odyssey.user;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @RolesAllowed("ADMIN")
    public User getUserById(@PathVariable("userId") Integer userId) {
        return userService.getUser(userId);
    }

    @PostMapping()
    public void registerUser(@RequestBody UserRegistrationRequest request) {
        userService.addUser(request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public void updateCustomer(@PathVariable("userId") Integer userId, @RequestBody UserUpdateRequest updateRequest) {
        userService.updateUser(userId, updateRequest);
    }
}
