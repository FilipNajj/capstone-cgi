package com.axlebank.transactionservice.util.exceptions.clientexceptions;

public class ClientIdLengthException extends RuntimeException {
    public ClientIdLengthException(String message) {
        super(message);
    }
}
