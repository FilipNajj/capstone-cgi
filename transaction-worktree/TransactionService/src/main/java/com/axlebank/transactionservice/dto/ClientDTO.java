package com.axlebank.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private int profileId;
    private String firstName;
    private String lastName;
    private String email;
    private ProfileStatus profileStatus;

}

