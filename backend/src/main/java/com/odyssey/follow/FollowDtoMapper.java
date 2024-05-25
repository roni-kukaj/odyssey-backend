package com.odyssey.follow;

import com.odyssey.user.UserDtoMapper;
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
