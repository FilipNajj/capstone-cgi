package com.axlebank.transactionservice.util.exceptions.institutionexceptions;

public class InstitutionNotPresentException extends RuntimeException {
    public InstitutionNotPresentException(String message) {
        super(message);
    }
}
