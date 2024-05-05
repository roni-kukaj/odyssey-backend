package com.odyssey.plans;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("planJPAService")
public class PlanJPADataAccessService implements PlanDao {

    private final PlanRepository planRepository;

    public PlanJPADataAccessService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> selectAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Optional<Plan> selectPlanById(Integer id) {
        return planRepository.findById(id);
    }

    @Override
    public List<Plan> selectPlanByUserId(Integer userId) {
        return planRepository.findByUserId(userId);
    }

    @Override
    public void insertPlan(Plan plan) {
        planRepository.save(plan);
    }

    @Override
    public void deletePlanById(Integer id) {
        planRepository.deleteById(id);
    }

    @Override
    public void deletePlanByUserId(Integer userId) {
        planRepository.deleteByUserId(userId);
    }

    @Override
    public boolean existsPlanById(Integer id) {
        return planRepository.existsPlanById(id);
    }

    @Override
    public boolean existsPlanByUserIdAndLocationId(Integer userId, Integer locationId) {
        return planRepository.existsPlanByUserIdAndLocationId(userId, locationId);
    }

    @Override
    public void updatePlan(Plan plan) {
        planRepository.save(plan);
    }


}
