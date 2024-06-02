package com.odyssey.controllers;

import com.odyssey.auth.AuthenticationRequest;
import com.odyssey.auth.AuthenticationResponse;
import com.odyssey.auth.AuthenticationService;
import com.odyssey.dtos.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PreAuthorize("#request.username == authentication.principal.username")
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody AuthenticationRequest request) {
        UserDto dto = authenticationService.verifyUser(request);
        return ResponseEntity.ok()
                .body(dto);
    }

}
