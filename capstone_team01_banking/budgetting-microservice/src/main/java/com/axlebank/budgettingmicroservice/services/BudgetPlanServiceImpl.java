package com.axlebank.budgettingmicroservice.services;

import com.axlebank.budgettingmicroservice.exceptions.BudgetPlanNotFoundException;
import com.axlebank.budgettingmicroservice.exceptions.ClientIdNotFoundException;
import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import com.axlebank.budgettingmicroservice.models.BudgetResult;
import com.axlebank.budgettingmicroservice.models.Transaction;
import com.axlebank.budgettingmicroservice.repository.BudgetPlanRepository;
import com.axlebank.budgettingmicroservice.requests.BudgetPlanData;
import com.axlebank.budgettingmicroservice.responses.BudgetDetailsResponseDTO;
import com.axlebank.budgettingmicroservice.utils.TransactionServiceCall;
import com.axlebank.budgettingmicroservice.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService{

    private static final String GET_ALL_TRANSACTION_API_URL = "/api/v1/transaction/";
    @Autowired
    private BudgetPlanRepository planRepository;
    @Autowired
    TransactionServiceCall transactionServiceCall;
    @Autowired
    Utility utility;
    @Override
    public List<BudgetPlan> getBudgetPlanByClientId(int clientId) {
        return planRepository.findByClientId(clientId);
    }

    @Override
    public BudgetPlan addNewBudgetPlan(BudgetPlanData budgetPlan) throws ClientIdNotFoundException {


        BudgetPlan budgetPlanResult = getCurrentBudgetPlanByClientId(budgetPlan.getClientId());

        if(budgetPlanResult == null){

            boolean clientExist = transactionServiceCall.checkClientId(budgetPlan.getClientId());
            if(clientExist){
                budgetPlanResult = new BudgetPlan();

                // set the values
                budgetPlanResult.setStartingDate(LocalDate.now());
                budgetPlanResult.setClientId(budgetPlan.getClientId());
                budgetPlan.setFixedAmount(budgetPlanResult.getFixedAmount());
                budgetPlanResult.setEndingDate(budgetPlanResult.getStartingDate().plusMonths(1));
                // register to the database and return with the budget plan id
                return planRepository.save(budgetPlanResult);
            } else {

                throw new ClientIdNotFoundException();
            }
        }
        return budgetPlanResult;



    }

    @Override
    public BudgetPlan editBudgetPlan(BudgetPlanData budgetPlan) throws BudgetPlanNotFoundException {

        BudgetPlan plan = planRepository.findTopByClientId(budgetPlan.getClientId());

        if(plan != null){

            plan.setFixedAmount(budgetPlan.getFixedAmount());
            return planRepository.save(plan);


        } else{
            throw new BudgetPlanNotFoundException(budgetPlan.getClientId());
        }
    }

    @Override
    public BudgetDetailsResponseDTO getBudgetResultInfoByClientId(int clientId) throws BudgetPlanNotFoundException {
        BudgetPlan budgetPlan = getCurrentBudgetPlanByClientId(clientId);

        if(budgetPlan == null){
            throw new BudgetPlanNotFoundException(clientId);
        }

        return getBugetDetailsInformation(budgetPlan);
    }

    private BudgetDetailsResponseDTO getBugetDetailsInformation(BudgetPlan budgetPlan) {

        // get all the transactions from the transaction microservice base on client id
        //List<Transaction> transactions = transactionServiceCall.getTransactionsOf(budgetPlan);

        List<Transaction> transactions = transactionServiceCall.getTransactionsOf(budgetPlan);



        // get the actual data in form of map of (payeeId and total spend amount)
        Map<String,Double> data_total_spending = utility.dataInformationsOf(transactions);

        double spentAmount = utility.getTotalSpent(data_total_spending);

        List<BudgetResult> budgetResultList = new ArrayList<>();

        data_total_spending.forEach((key, value) -> {


            BudgetResult budgetResult = new BudgetResult();
            budgetResult.setCategory(key);
            double percentage = (value * 100) / budgetPlan.getFixedAmount();
            budgetResult.setPercentage(percentage);
            budgetResultList.add(budgetResult);
        });

        BudgetDetailsResponseDTO responseDTO = new BudgetDetailsResponseDTO();
        responseDTO.setData(budgetResultList);
        responseDTO.setFixeAmount(budgetPlan.getFixedAmount());
        responseDTO.setTotalSpent(spentAmount);
        responseDTO.setStartingDate(budgetPlan.getStartingDate());
        responseDTO.setEndingDate(budgetPlan.getEndingDate());
        return responseDTO;
    }


    @Override
    public BudgetPlan getCurrentBudgetPlanByClientId(int clientId) {
        boolean addNewOne = goNextBudgetPlanPeriod(clientId);
        if(addNewOne){
            BudgetPlan budgetPlan = planRepository.findTopByClientId(clientId);
            BudgetPlan newBudgetPlan = new BudgetPlan();
            LocalDate now = LocalDate.now();
            newBudgetPlan.setFixedAmount(budgetPlan.getFixedAmount());
            newBudgetPlan.setStartingDate(now);
            newBudgetPlan.setEndingDate(now.plusMonths(1));
            newBudgetPlan.setClientId(budgetPlan.getClientId());

            return planRepository.save(newBudgetPlan);

        }
        List<BudgetPlan> all = planRepository.findByClientId(clientId);
        return all.isEmpty() ? null : all.get(all.size()-1);

    }

    @Override
    public List<BudgetDetailsResponseDTO> findAllForClient(int clientId) {

        List<BudgetPlan> plans = planRepository.findByClientId(clientId);


        List<BudgetDetailsResponseDTO> responseDTOS = new ArrayList<>();
        plans.forEach(plan->{

            BudgetDetailsResponseDTO responseDTO= getBugetDetailsInformation(plan);

            responseDTOS.add(responseDTO);
        });

        return responseDTOS;
    }

    private boolean goNextBudgetPlanPeriod(int clientId){
        List<BudgetPlan> all = planRepository.findByClientId(clientId);
        BudgetPlan budgetPlanResult = all.isEmpty() ? null : all.get(all.size()-1);

        if(budgetPlanResult != null){

            LocalDate now = LocalDate.now();

            return now.isAfter(budgetPlanResult.getEndingDate());

        } else {
          return false;
        }
    }


}
