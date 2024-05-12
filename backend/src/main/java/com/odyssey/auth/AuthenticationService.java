package com.odyssey.auth;

import com.odyssey.config.JwtService;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.user.UserRegistrationRequest;
import com.odyssey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.odyssey.role.Role;
import com.odyssey.user.User;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(UserRegistrationRequest request) {
        var user = User.builder()
                .fullname(request.fullname())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .avatar(request.avatar())
                .role(new Role(1, "USER"))
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateUserToken(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = repository.findByUsernameAndRoleId(request.getUsername() ,1)
                .orElseThrow();
        var jwtToken = jwtService.generateUserToken(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = repository.findByUsernameAndRoleId(request.getUsername() ,2)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        var jwtToken = jwtService.generateUserToken(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateHeadAdmin(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = repository.findByUsernameAndRoleId(request.getUsername() ,3)
                .orElseThrow();
        var jwtToken = jwtService.generateUserToken(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
