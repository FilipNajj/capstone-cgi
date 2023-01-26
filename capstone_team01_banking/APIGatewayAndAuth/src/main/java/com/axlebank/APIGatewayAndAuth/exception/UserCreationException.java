package com.axlebank.APIGatewayAndAuth.exception;

public class UserCreationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserCreationException(String s) {
        super(s);
    }
}
