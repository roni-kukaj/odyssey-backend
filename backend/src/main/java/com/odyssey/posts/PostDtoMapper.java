package com.odyssey.posts;

import com.odyssey.trips.TripDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostDtoMapper implements Function<Post, PostDto> {

    private final TripDtoMapper tripDtoMapper = new TripDtoMapper();

    @Override
    public PostDto apply(Post post) {
        return new PostDto(
                post.getId(),
                post.getText(),
                post.getImage(),
                post.getPostedTime(),
                tripDtoMapper.apply(post.getTrip())
        );
    }
}
