package com.axlebank.transactionservice.util.exceptions.transactionexceptions;

public class TransactionNotPresentException extends RuntimeException {
    public TransactionNotPresentException() {
        super("Transaction not present");
    }
}
