package com.odyssey.services.utils;

import com.odyssey.dtos.PlanDto;
import com.odyssey.models.Plan;
import com.odyssey.services.utils.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PlanDtoMapper implements Function<Plan, PlanDto> {

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public PlanDto apply(Plan plan) {
        return new PlanDto(
                plan.getId(),
                plan.getVisitDate(),
                userDtoMapper.apply(plan.getUser()),
                plan.getLocation()
        );
    }
}
