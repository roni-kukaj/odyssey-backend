package com.odyssey.services.utils;

import com.odyssey.dtos.PostDto;
import com.odyssey.models.Post;
import com.odyssey.services.utils.TripDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostDtoMapper implements Function<Post, PostDto> {

    private final TripDtoMapper tripDtoMapper = new TripDtoMapper();
    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public PostDto apply(Post post) {
        return new PostDto(
                post.getId(),
                post.getText(),
                post.getImage(),
                post.getPostedTime(),
                userDtoMapper.apply(post.getUser()),
                tripDtoMapper.apply(post.getTrip())
        );
    }
}
