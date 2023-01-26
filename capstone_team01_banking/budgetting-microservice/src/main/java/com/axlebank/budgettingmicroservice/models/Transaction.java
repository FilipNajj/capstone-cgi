package com.axlebank.budgettingmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {


    private int transactionId;

    private double amount;

    private LocalDate dateCreated;

    private TransactionType transactionType;
    private String userType;

    private int userId;
    private int institutionId;
}
