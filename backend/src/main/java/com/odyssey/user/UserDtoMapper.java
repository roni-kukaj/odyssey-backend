package com.odyssey.user;

import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        return new UserDto(
            user.getId(),
            user.getFullname(),
            user.getUsername(),
            user.getEmail(),
            user.getAvatar(),
            user.getRole()
        );
    }
}
