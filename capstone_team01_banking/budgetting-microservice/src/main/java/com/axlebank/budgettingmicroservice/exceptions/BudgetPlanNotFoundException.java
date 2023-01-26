package com.axlebank.budgettingmicroservice.exceptions;



public class BudgetPlanNotFoundException extends Exception {

     public BudgetPlanNotFoundException(int clientId){

         super("the budget plan with client id=" + clientId+" doesn't exist!");

     }
}
