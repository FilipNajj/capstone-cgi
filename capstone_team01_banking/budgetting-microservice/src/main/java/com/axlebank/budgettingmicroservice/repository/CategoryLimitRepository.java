package com.axlebank.budgettingmicroservice.repository;

import com.axlebank.budgettingmicroservice.models.BudgetResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryLimitRepository extends JpaRepository<BudgetResult,String> {
}
