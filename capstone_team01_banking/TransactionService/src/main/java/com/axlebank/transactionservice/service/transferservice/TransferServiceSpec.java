package com.axlebank.transactionservice.service.transferservice;

import com.axlebank.transactionservice.model.Transaction;

import java.util.List;

public interface TransferServiceSpec {
    List<Transaction> makeTransfer(Transaction transactionRequested, int clientId);
}
