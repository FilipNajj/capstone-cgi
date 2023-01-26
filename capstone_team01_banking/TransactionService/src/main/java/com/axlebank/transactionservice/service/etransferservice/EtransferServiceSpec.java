package com.axlebank.transactionservice.service.etransferservice;

import com.axlebank.transactionservice.model.Transaction;

import java.util.List;

public interface EtransferServiceSpec {
    List<Transaction> makeETransfer(Transaction transactionRequested, int clientId);
}
