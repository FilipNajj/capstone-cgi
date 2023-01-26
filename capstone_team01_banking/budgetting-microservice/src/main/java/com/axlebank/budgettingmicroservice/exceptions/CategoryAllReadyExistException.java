package com.axlebank.budgettingmicroservice.exceptions;

public class CategoryAllReadyExistException extends Exception {
    public CategoryAllReadyExistException(String category) {
        super(String.format("the category name={%s} is already existing",category));
    }
}
