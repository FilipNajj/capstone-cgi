package com.axlebank.budgettingmicroservice.exceptions;

public class CategoryLimitNotFound extends Exception {

    public CategoryLimitNotFound() {
        super("the current category wasn't set to a percentage limit");
    }

    public CategoryLimitNotFound(String message) {
        super(message);
    }
}
