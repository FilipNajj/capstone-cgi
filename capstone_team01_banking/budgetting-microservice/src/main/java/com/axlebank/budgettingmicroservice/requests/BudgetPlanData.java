package com.axlebank.budgettingmicroservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetPlanData {

    private double fixedAmount;

    private int clientId;
}
