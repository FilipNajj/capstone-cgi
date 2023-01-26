package com.axlebank.budgettingmicroservice.controller;


import com.axlebank.budgettingmicroservice.exceptions.CategoryAllReadyExistException;
import com.axlebank.budgettingmicroservice.exceptions.CategoryNameNotFound;
import com.axlebank.budgettingmicroservice.models.BudgetResult;
import com.axlebank.budgettingmicroservice.models.ClientCategoryLimit;
import com.axlebank.budgettingmicroservice.services.CategoryLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/budget/limit")
public class CategoryLimitController {



    @Autowired
    private CategoryLimitService categoryLimitService;


    @PostMapping("")
    public ResponseEntity<?> add_new_limit(@RequestBody ClientCategoryLimit budgetResult){

        System.out.println("the budget result=>" + budgetResult);
        try {
            return new ResponseEntity<>(categoryLimitService.addNewLimit(budgetResult), HttpStatus.OK);
        } catch (CategoryNameNotFound e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> get_category_limit(@PathVariable int clientId){
        return new ResponseEntity<>(categoryLimitService.getClientCustomCategoriesLimit(clientId),HttpStatus.OK);
    }


    @PutMapping("")
    public ResponseEntity<?> updateLimit(@RequestBody ClientCategoryLimit budgetResult){
        try {
            return new ResponseEntity<>(categoryLimitService.editCategoryLimit(budgetResult),HttpStatus.OK);
        } catch (CategoryNameNotFound e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> budgetResultList(){
        return new ResponseEntity<>(categoryLimitService.findAll(),HttpStatus.OK);
    }

    @PostMapping("/new-category")
    public ResponseEntity<?> addNewGlobalCategory(@RequestBody BudgetResult category){

        try {
            return new ResponseEntity<>(categoryLimitService.addNewCategory(category),HttpStatus.CREATED);
        } catch (CategoryAllReadyExistException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

}
