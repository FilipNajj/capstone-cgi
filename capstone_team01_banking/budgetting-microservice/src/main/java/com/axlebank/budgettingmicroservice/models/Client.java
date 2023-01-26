package com.axlebank.budgettingmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {


    private int profileId;
    private String firstName;

    private String lastName;
}
