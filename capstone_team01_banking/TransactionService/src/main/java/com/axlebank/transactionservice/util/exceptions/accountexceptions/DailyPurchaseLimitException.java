package com.axlebank.transactionservice.util.exceptions.accountexceptions;

public class DailyPurchaseLimitException extends RuntimeException {
    public DailyPurchaseLimitException(String message) {
        super(message);
    }
}
