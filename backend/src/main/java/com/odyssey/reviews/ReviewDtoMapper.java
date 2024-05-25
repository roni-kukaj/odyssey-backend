package com.odyssey.reviews;

import com.odyssey.user.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ReviewDtoMapper implements Function<Review, ReviewDto> {
    private final UserDtoMapper userDtoMapper = new UserDtoMapper();
    @Override
    public ReviewDto apply(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getDescription(),
                review.getRating(),
                userDtoMapper.apply(review.getUser()),
                review.getLocation()
        );
    }
}
