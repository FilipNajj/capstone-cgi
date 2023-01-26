package com.axlebank.transactionservice.service.depositservice;

import com.axlebank.transactionservice.model.Transaction;

import java.util.List;

public interface DepositServiceSpec{
    List<Transaction> makeDeposit(Transaction transactionRequested, int clientId);
}
