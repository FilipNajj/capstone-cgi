package com.axlebank.transactionservice.util.exceptions.accountexceptions;

public class AccountNotBelongsToClientException extends RuntimeException {
    public AccountNotBelongsToClientException(String message) {
        super(message);
    }
}
