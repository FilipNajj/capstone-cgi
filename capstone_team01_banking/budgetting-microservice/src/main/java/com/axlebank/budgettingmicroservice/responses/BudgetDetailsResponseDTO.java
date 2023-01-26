package com.axlebank.budgettingmicroservice.responses;

import com.axlebank.budgettingmicroservice.models.BudgetResult;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class BudgetDetailsResponseDTO {



    private double fixeAmount;

    private double totalSpent;

    private LocalDate startingDate;
    private LocalDate endingDate;
    private List<BudgetResult> data;

}
