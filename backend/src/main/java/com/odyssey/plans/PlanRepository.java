package com.odyssey.plans;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    boolean existsPlanById(Integer Id);
    List<Plan> findByUserId(Integer userId);
    boolean existsPlanByUserIdAndLocationId(Integer userId, Integer locationId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Plan p WHERE p.user.id = :userId")
    void deleteByUserId(Integer userId);
}
