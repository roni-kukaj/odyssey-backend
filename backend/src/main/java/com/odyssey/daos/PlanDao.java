package com.odyssey.daos;

import com.odyssey.models.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanDao {
    List<Plan> selectAllPlans();
    List<Plan> selectPlanByUserId(Integer userId);
    void insertPlan(Plan plan);
    void deletePlanById(Integer id);
    void deletePlanByUserId(Integer userId);
    void updatePlan(Plan plan);
    boolean existsPlanById(Integer id);
    boolean existsPlanByUserIdAndLocationId(Integer userId, Integer locationId);
    Optional<Plan> selectPlanById(Integer id);
}