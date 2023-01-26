package com.axlebank.transactionservice.util.exceptions.transactionexceptions;

public class AmountRequestedException extends RuntimeException {
    public AmountRequestedException(String message) {
        super(message);
    }
}
