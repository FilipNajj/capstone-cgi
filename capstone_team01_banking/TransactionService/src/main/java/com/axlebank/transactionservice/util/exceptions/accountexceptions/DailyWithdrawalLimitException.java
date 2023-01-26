package com.axlebank.transactionservice.util.exceptions.accountexceptions;

public class DailyWithdrawalLimitException extends RuntimeException {
    public DailyWithdrawalLimitException(String message) {
        super(message);
    }
}
