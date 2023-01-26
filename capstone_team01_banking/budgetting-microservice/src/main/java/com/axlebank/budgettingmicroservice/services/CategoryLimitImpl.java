package com.axlebank.budgettingmicroservice.services;

import com.axlebank.budgettingmicroservice.exceptions.CategoryAllReadyExistException;
import com.axlebank.budgettingmicroservice.exceptions.CategoryLimitNotFound;
import com.axlebank.budgettingmicroservice.exceptions.CategoryNameNotFound;
import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import com.axlebank.budgettingmicroservice.models.BudgetResult;
import com.axlebank.budgettingmicroservice.models.ClientCategoryLimit;
import com.axlebank.budgettingmicroservice.models.ClientCustomInfoId;
import com.axlebank.budgettingmicroservice.repository.BudgetPlanRepository;
import com.axlebank.budgettingmicroservice.repository.CategoryLimitRepository;
import com.axlebank.budgettingmicroservice.repository.ClientCategoryLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryLimitImpl implements   CategoryLimitService{

    @Autowired
    private CategoryLimitRepository repository;

    @Autowired
    private ClientCategoryLimitRepository clientCategoryLimitRepository;
    @Override
    public ClientCategoryLimit addNewLimit(ClientCategoryLimit result) throws CategoryNameNotFound {
        BudgetResult catData= repository.findById(result.getCategory()).orElse(null);

        if(catData != null){
            ClientCustomInfoId id = new ClientCustomInfoId(result.getClientId(),result.getCategory());
            ClientCategoryLimit clientCategoryLimit
                    = clientCategoryLimitRepository.findById(id).orElse(null);
            if(clientCategoryLimit == null){

                return clientCategoryLimitRepository.save(result);
            } else return result;

        }
        else throw new CategoryNameNotFound();
    }

    @Override
    public ClientCategoryLimit editCategoryLimit(ClientCategoryLimit newData) throws CategoryNameNotFound {
        BudgetResult catData= repository.findById(newData.getCategory()).orElse(null);
        if(catData != null){

            return clientCategoryLimitRepository.save(newData);
        }

        throw new CategoryNameNotFound();
    }

    @Override
    public ClientCategoryLimit getCategoryLimitData(String categoryName,int clientId) throws CategoryLimitNotFound {

        ClientCategoryLimit result = clientCategoryLimitRepository
                .findById(new ClientCustomInfoId(clientId,categoryName))
                .orElse(null);

        if(result == null){
            throw new CategoryLimitNotFound();
        } else return result;
    }

    @Override
    public List<ClientCategoryLimit> getClientCustomCategoriesLimit(int clientId) {

        return findByClientId(clientId);
    }

    private List<ClientCategoryLimit> findByClientId(int clientId) {
        return clientCategoryLimitRepository.findAll()
                .stream()
                .filter(input-> input.getClientId() == clientId)
                .collect(Collectors.toList());
    }

    @Override
    public List<BudgetResult> findAll() {
        return repository.findAll();
    }

    @Override
    public BudgetResult addNewCategory(BudgetResult category) throws CategoryAllReadyExistException {
        BudgetResult budgetResult = repository.findById(category.getCategory()).orElse(null);
        if(budgetResult == null){
            return repository.save(category);
        }
        throw new CategoryAllReadyExistException(category.getCategory());
    }
}
