package com.axlebank.budgettingmicroservice.repository;

import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BudgetPlanRepository extends JpaRepository<BudgetPlan,Long> {


    List<BudgetPlan> findByClientId(int clientId);
    BudgetPlan findTopByClientId(int clientId);

}
