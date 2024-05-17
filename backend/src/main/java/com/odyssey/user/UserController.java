package com.odyssey.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<User> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
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
    public void updateUserInformation(
            @PathVariable("userId") Integer userId,
            @RequestBody UserUpdateInformationDto dto
    ) {
        userService.updateUser(userId, dto);
    }

    @PutMapping(value = "/{userId}/avatar", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void updateUserAvatar(
            @PathVariable("userId") Integer userId,
            @RequestPart("file") MultipartFile file
    ) {
        userService.updateUserAvatar(userId, file);
    }

}
