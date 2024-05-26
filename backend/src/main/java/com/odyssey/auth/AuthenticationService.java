package com.odyssey.auth;

import com.odyssey.jwt.JWTUtil;
import com.odyssey.models.User;
import com.odyssey.dtos.UserDto;
import com.odyssey.services.utils.UserDtoMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper mapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, UserDtoMapper mapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User principal = (User) authentication.getPrincipal();
        UserDto userDto = mapper.apply(principal);
        String token = jwtUtil.issueToken(userDto.username(), userDto.role().getName());
        return new AuthenticationResponse(token);
    }
}
