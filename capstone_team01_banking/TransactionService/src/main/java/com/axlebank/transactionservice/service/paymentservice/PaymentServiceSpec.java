package com.axlebank.transactionservice.service.paymentservice;

import com.axlebank.transactionservice.model.Transaction;

import java.util.List;

public interface PaymentServiceSpec {
    List<Transaction> makePayment(Transaction transactionRequested, int clientId);
}
