package com.axlebank.budgettingmicroservice.services;

import com.axlebank.budgettingmicroservice.exceptions.CategoryAllReadyExistException;
import com.axlebank.budgettingmicroservice.exceptions.CategoryLimitNotFound;
import com.axlebank.budgettingmicroservice.exceptions.CategoryNameNotFound;
import com.axlebank.budgettingmicroservice.models.BudgetResult;
import com.axlebank.budgettingmicroservice.models.ClientCategoryLimit;

import java.util.List;

public interface CategoryLimitService {

    ClientCategoryLimit addNewLimit(ClientCategoryLimit result) throws CategoryNameNotFound;
    ClientCategoryLimit editCategoryLimit(ClientCategoryLimit newData) throws CategoryNameNotFound;
    ClientCategoryLimit getCategoryLimitData(String categoryName,int clientId) throws CategoryLimitNotFound;

    List<ClientCategoryLimit> getClientCustomCategoriesLimit(int clientId);
    List<BudgetResult> findAll();

    BudgetResult addNewCategory(BudgetResult category) throws CategoryAllReadyExistException;
}
