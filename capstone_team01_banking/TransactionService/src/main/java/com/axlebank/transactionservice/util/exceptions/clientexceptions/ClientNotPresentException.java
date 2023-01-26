package com.axlebank.transactionservice.util.exceptions.clientexceptions;

public class ClientNotPresentException extends RuntimeException {
    public ClientNotPresentException(String message) {
        super(message);
    }
}
