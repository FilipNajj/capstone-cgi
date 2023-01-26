package com.axlebank.budgettingmicroservice.exceptions;

import com.axlebank.budgettingmicroservice.services.CategoryLimitService;

public class ClientIdNotFoundException  extends Exception{

    public ClientIdNotFoundException(String message){
        super(message);
    }

    public ClientIdNotFoundException(){

        super("the client id doesn't exist!");
    }
}
