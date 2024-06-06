package com.odyssey.services.utils;

import com.odyssey.dtos.UserDto;
import com.odyssey.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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
