package com.odyssey.plans;

import com.odyssey.models.Plan;
import com.odyssey.repositories.PlanRepository;
import com.odyssey.services.data.PlanJPADataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PlanJpaDataAccessServiceTest {
    private PlanJPADataAccessService planDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private PlanRepository planRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        planDataAccessService = new PlanJPADataAccessService(planRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllPlans() {
        planDataAccessService.selectAllPlans();
        verify(planRepository).findAll();
    }

    @Test
    void selectPlanById() {
        int id = 1;
        planDataAccessService.selectPlanById(id);
        verify(planRepository).findById(id);
    }

    @Test
    void selectPlansByUserId() {
        int userId = 1;
        planDataAccessService.selectPlanByUserId(userId);
        verify(planRepository).findByUserId(userId);
    }

    @Test
    void insertPlan() {
        Plan plan = new Plan();
        planDataAccessService.insertPlan(plan);
        verify(planRepository).save(plan);
    }

    @Test
    void deletePlanById() {
        int id = 1;
        planDataAccessService.deletePlanById(id);
        verify(planRepository).deleteById(id);
    }

    @Test
    void deletePlanByUserId() {
        int id = 1;
        planDataAccessService.deletePlanByUserId(id);
        verify(planRepository).deleteByUserId(id);
    }

    @Test
    void existsPlanById() {
        int id = 1;
        planDataAccessService.existsPlanById(id);
        verify(planRepository).existsPlanById(id);
    }

    @Test
    void existsPlanByUserIdAndLocationId() {
        int userId = 1;
        int locationId = 1;
        planDataAccessService.existsPlanByUserIdAndLocationId(userId, locationId);
        verify(planRepository).existsPlanByUserIdAndLocationId(userId, locationId);
    }

    @Test
    void updatePlan() {
        Plan plan = new Plan();
        planDataAccessService.updatePlan(plan);
        verify(planRepository).save(plan);
    }

}
