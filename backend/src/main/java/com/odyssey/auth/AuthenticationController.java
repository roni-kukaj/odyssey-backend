package com.odyssey.auth;
import com.odyssey.user.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserRegistrationRequest request
    ) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    @PostMapping("/authenticate-user")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticateUser(request));
    }

    @PostMapping("/authenticate-admin")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticateAdmin(request));
    }

    @PostMapping("/authenticate-headadmin")
    public ResponseEntity<AuthenticationResponse> authenticateHeadAdmin(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticateHeadAdmin(request));
    }

}
