package com.odyssey.services;

import com.odyssey.daos.PlanDao;
import com.odyssey.dtos.PlanDto;
import com.odyssey.dtos.PlanRegistrationRequest;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Plan;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.PlanUpdateRequest;
import com.odyssey.services.utils.PlanDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final PlanDao planDao;
    private final UserDao userDao;
    private final LocationDao locationDao;
    private final PlanDtoMapper planDtoMapper;

    public PlanService(
            @Qualifier("planJPAService") PlanDao planDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("locationJPAService") LocationDao locationDao,
            PlanDtoMapper planDtoMapper
    ) {
        this.planDao = planDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
        this.planDtoMapper = planDtoMapper;
    }

    private Plan getPlanById(Integer id) {
        return planDao.selectPlanById(id)
                .orElseThrow(() -> new ResourceNotFoundException("plan with id [%s] not found".formatted(id)));
    }

    public List<PlanDto> getAllPlans() {
        return planDao.selectAllPlans()
                .stream().map(planDtoMapper).collect(Collectors.toList());
    }

    public List<PlanDto> getPlansByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return planDao.selectPlanByUserId(userId)
                .stream().map(planDtoMapper).collect(Collectors.toList());
    }

    public PlanDto getPlan(Integer id) {
        return planDao.selectPlanById(id)
                .map(planDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("plan with id [%s] not found".formatted(id)));
    }

    public void addPlan(PlanRegistrationRequest request) {
        User user = userDao.selectUserById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.userId())));

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.userId())));

        LocalDate date = request.date();
        Plan plan = new Plan(user, location, date);

        if (planDao.existsPlanByUserIdAndLocationId(request.userId(), request.locationId())) {
            throw new DuplicateResourceException("plan already exists");
        }

        planDao.insertPlan(plan);
    }

    public void deletePlan(Integer id) {
        if (planDao.existsPlanById(id)) {
            planDao.deletePlanById(id);
        }
        else {
            throw new ResourceNotFoundException("plan with id [%s] not found".formatted(id));
        }
    }

    public void deletePlansByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        planDao.deletePlanByUserId(userId);
    }

    public void updatePlan(Integer id, PlanUpdateRequest request) {
        Plan existingPlan = getPlanById(id);

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        boolean changes = false;

        if (request.locationId() != null && !request.locationId().equals(existingPlan.getLocation().getId())) {
            existingPlan.setLocation(location);
            changes = true;
        }
        if (request.date() != null && !request.date().equals(existingPlan.getVisitDate())) {
            existingPlan.setVisitDate(request.date());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        planDao.updatePlan(existingPlan);
    }

}
