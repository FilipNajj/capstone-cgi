package com.axlebank.transactionservice.util.exceptions.accountexceptions;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(String message) {
        super(message);
    }
}
