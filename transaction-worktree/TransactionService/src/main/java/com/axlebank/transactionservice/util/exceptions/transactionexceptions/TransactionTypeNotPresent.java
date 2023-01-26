package com.axlebank.transactionservice.util.exceptions.transactionexceptions;

public class TransactionTypeNotPresent extends RuntimeException {
    public TransactionTypeNotPresent(String message) {
        super(message);
    }
}
