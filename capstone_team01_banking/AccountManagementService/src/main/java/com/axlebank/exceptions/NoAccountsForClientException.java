package com.axlebank.exceptions;

public class NoAccountsForClientException extends RuntimeException {
    public NoAccountsForClientException(String message) {
        super(message);
    }
}
