package com.axlebank.transactionservice.util.exceptions.transactionexceptions;

public class WithdrawalTransactionMediumException extends RuntimeException {
    public WithdrawalTransactionMediumException(String message) {
        super(message);
    }
}
