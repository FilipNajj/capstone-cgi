package com.axlebank.budgettingmicroservice.services;

import com.axlebank.budgettingmicroservice.exceptions.BudgetPlanNotFoundException;
import com.axlebank.budgettingmicroservice.exceptions.ClientIdNotFoundException;
import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import com.axlebank.budgettingmicroservice.models.BudgetResult;
import com.axlebank.budgettingmicroservice.repository.BudgetPlanRepository;
import com.axlebank.budgettingmicroservice.requests.BudgetPlanData;
import com.axlebank.budgettingmicroservice.responses.BudgetDetailsResponseDTO;

import java.util.List;

public interface BudgetPlanService {


    List<BudgetPlan> getBudgetPlanByClientId(int clientId);

    BudgetPlan addNewBudgetPlan(BudgetPlanData budgetPlan) throws ClientIdNotFoundException;

    BudgetPlan editBudgetPlan(BudgetPlanData budgetPlan) throws BudgetPlanNotFoundException;

    BudgetDetailsResponseDTO getBudgetResultInfoByClientId(int clientId) throws BudgetPlanNotFoundException;
    public BudgetPlan getCurrentBudgetPlanByClientId(int clientId);


    List<BudgetDetailsResponseDTO> findAllForClient(int clientId);
}
