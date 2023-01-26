package com.axlebank.transactionservice.util.exceptions.accountexceptions;

public class AccountNotPresentException extends RuntimeException {
    public AccountNotPresentException(String message) {
        super(message);
    }
}
