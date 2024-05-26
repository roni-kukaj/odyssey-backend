package com.odyssey.controllers;

import com.odyssey.jwt.JWTUtil;
import com.odyssey.dtos.UserDto;
import com.odyssey.dtos.UserRegistrationRequest;
import com.odyssey.services.UserService;
import com.odyssey.dtos.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping
    public List<UserDto> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Integer userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        userService.addUser(request);
        String jwtToken = jwtUtil.issueToken(request.username(), "USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @PreAuthorize("hasAuthority('MAINADMIN')")
    @PostMapping("/admin")
    public void registerAdmin(@RequestBody UserRegistrationRequest request) {
        userService.addAdmin(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);
    }

    @PreAuthorize("hasAuthority('USER') and #userId == authentication.principal.userId")
    @PutMapping("/{userId}")
    public void updateUserInformation(
            @PathVariable("userId") Integer userId,
            @ModelAttribute UserUpdateDto dto
    ) {
        userService.updateUser(userId, dto);
    }

}
