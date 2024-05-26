package com.odyssey.services.utils;

import com.odyssey.dtos.FollowDto;
import com.odyssey.models.Follow;
import com.odyssey.services.utils.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FollowDtoMapper implements Function<Follow, FollowDto> {

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public FollowDto apply(Follow follow) {
        return new FollowDto(
                follow.getId(),
                userDtoMapper.apply(follow.getFollower()),
                userDtoMapper.apply(follow.getFollowing())
        );
    }
}
