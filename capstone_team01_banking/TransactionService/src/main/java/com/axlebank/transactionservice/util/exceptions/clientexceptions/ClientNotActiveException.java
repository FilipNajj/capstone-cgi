package com.axlebank.transactionservice.util.exceptions.clientexceptions;

public class ClientNotActiveException extends RuntimeException {
    public ClientNotActiveException(String message) {
        super(message);
    }
}
