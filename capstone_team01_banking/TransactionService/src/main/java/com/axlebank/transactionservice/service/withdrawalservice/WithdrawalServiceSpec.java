package com.axlebank.transactionservice.service.withdrawalservice;

import com.axlebank.transactionservice.model.Transaction;

import java.util.List;

public interface WithdrawalServiceSpec {
    List<Transaction> makeWithdrawal(Transaction transactionRequested, int clientId);
}
