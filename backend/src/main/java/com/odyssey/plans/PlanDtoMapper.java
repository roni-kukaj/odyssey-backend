package com.odyssey.plans;

import com.odyssey.user.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PlanDtoMapper implements Function<Plan, PlanDto> {

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public PlanDto apply(Plan plan) {
        return new PlanDto(
                plan.getId(),
                userDtoMapper.apply(plan.getUser()),
                plan.getLocation()
        );
    }
}
