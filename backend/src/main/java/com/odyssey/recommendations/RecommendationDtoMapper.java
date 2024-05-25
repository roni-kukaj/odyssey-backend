package com.odyssey.recommendations;

import com.odyssey.user.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RecommendationDtoMapper implements Function<Recommendation, RecommendationDto> {

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public RecommendationDto apply(Recommendation recommendation) {
        return new RecommendationDto(
                recommendation.getId(),
                recommendation.getDescription(),
                userDtoMapper.apply(recommendation.getUser()),
                recommendation.getActivity()
        );
    }
}
