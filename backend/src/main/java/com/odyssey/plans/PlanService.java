package com.odyssey.plans;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlanService {

    private final PlanDao planDao;
    private final UserDao userDao;
    private final LocationDao locationDao;

    public PlanService(
            @Qualifier("planJPAService") PlanDao planDao,
            @Qualifier("userJPAService")UserDao userDao,
            @Qualifier("locationJPAService") LocationDao locationDao) {
        this.planDao = planDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
    }

    public List<Plan> getAllPlans() {
        return planDao.selectAllPlans();
    }

    public List<Plan> getPlansByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return planDao.selectPlanByUserId(userId);
    }

    public Plan getPlan(Integer id) {
        return planDao.selectPlanById(id)
                .orElseThrow(() -> new ResourceNotFoundException("plan with id [%s] not found".formatted(id)));
    }

    public void addPlan(PlanRegistrationRequest request) {
        User user = userDao.selectUserById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.userId())));

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.userId())));

        Date date = request.date();
        Plan plan = new Plan(user, location, date);

        if (planDao.existsPlanByUserIdAndLocationId(request.userId(), request.locationId())) {
            throw new DuplicateResourceException("plan already exists");
        }

        planDao.insertPlan(plan);
    }

    public boolean deletePlan(Integer id) {
        if (planDao.existsPlanById(id)) {
            planDao.deletePlanById(id);
        }
        else {
            throw new ResourceNotFoundException("plan with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean deletePlansByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        planDao.deletePlanByUserId(userId);
        return false;
    }

    public boolean updatePlan(Integer id, PlanUpdateRequest request) {
        Plan existingPlan = getPlan(id);

        Location location = locationDao.selectLocationById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(request.locationId())));

        User user = userDao.selectUserById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.userId())));

        if (!request.userId().equals(existingPlan.getUser().getId())) {
            throw new RequestValidationException("cannot change the user");
        }

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
        return changes;
    }

}
