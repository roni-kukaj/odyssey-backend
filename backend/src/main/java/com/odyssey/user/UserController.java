package com.odyssey.user;

import com.odyssey.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        userService.addUser(request);
        String jwtToken = jwtUtil.issueToken(request.username(), "USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public void updateUserInformation(
            @PathVariable("userId") Integer userId,
            @ModelAttribute UserUpdateDto dto
    ) {
        userService.updateUser(userId, dto);
    }

}
