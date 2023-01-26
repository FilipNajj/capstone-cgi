package com.axlebank.transactionservice.util.exceptions.transactionexceptions;

public class TransactionFailedException extends RuntimeException {
    public TransactionFailedException(String message) {
        super(message);
    }
}
