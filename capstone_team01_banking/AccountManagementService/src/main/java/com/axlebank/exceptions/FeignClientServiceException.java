package com.axlebank.exceptions;

public class FeignClientServiceException extends RuntimeException {
    public FeignClientServiceException(String message) {
        super(message);
    }
}
