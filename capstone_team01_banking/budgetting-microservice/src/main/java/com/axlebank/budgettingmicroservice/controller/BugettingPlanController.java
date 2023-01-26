package com.axlebank.budgettingmicroservice.controller;


import com.axlebank.budgettingmicroservice.exceptions.BudgetPlanNotFoundException;
import com.axlebank.budgettingmicroservice.exceptions.ClientIdNotFoundException;
import com.axlebank.budgettingmicroservice.requests.BudgetPlanData;
import com.axlebank.budgettingmicroservice.responses.BudgetDetailsResponseDTO;
import com.axlebank.budgettingmicroservice.services.BudgetPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/budgetPlan")
@RequiredArgsConstructor
public class BugettingPlanController {


    @Autowired
    private BudgetPlanService budgetPlanService;

    public BugettingPlanController(BudgetPlanService budgetPlanService) {
        this.budgetPlanService = budgetPlanService;

    }



    @PostMapping("/activate")
    public ResponseEntity<?> addNewBudget(@RequestBody BudgetPlanData budgetPlan){

        try {
            return new ResponseEntity<>(budgetPlanService.addNewBudgetPlan(budgetPlan), HttpStatus.CREATED);
        } catch (ClientIdNotFoundException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




    @GetMapping("{clientId}")
    public ResponseEntity<?> getAllPeriodDetails(@PathVariable int clientId){

        List<BudgetDetailsResponseDTO> data =  budgetPlanService.findAllForClient(clientId);

        return data.isEmpty() ? new ResponseEntity<>(
                new BudgetPlanNotFoundException(clientId).getMessage(),HttpStatus.NOT_FOUND)
                                : new ResponseEntity<>(data,HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<?> getBudgetPlanDetail(@RequestBody BudgetPlanData budgetPlan){
        try {
            BudgetDetailsResponseDTO responseDTO =
                    budgetPlanService.getBudgetResultInfoByClientId(budgetPlan.getClientId());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        } catch (BudgetPlanNotFoundException e) {
            return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> editBudgetPlan(@RequestBody BudgetPlanData budgetPlanData){

        try {
            return new ResponseEntity<>(budgetPlanService.editBudgetPlan(budgetPlanData),
                    HttpStatus.OK);
        } catch (BudgetPlanNotFoundException e) {
           return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }


    }



    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping("/endpoints")
    public ResponseEntity<List<String>> getEndpoints() {
        return new ResponseEntity<>(
                requestMappingHandlerMapping
                        .getHandlerMethods()
                        .keySet()
                        .stream()
                        .map(RequestMappingInfo::toString)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }




}
