package com.axlebank.budgettingmicroservice.exceptions;

public class CategoryNameNotFound extends Exception {

    public CategoryNameNotFound() {
        super("the category name is not found");
    }

    public CategoryNameNotFound(String message) {
        super(message);
    }
}
