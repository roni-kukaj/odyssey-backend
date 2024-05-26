package com.odyssey.services.utils;

import com.odyssey.dtos.RecommendationDto;
import com.odyssey.models.Recommendation;
import com.odyssey.services.utils.UserDtoMapper;
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
