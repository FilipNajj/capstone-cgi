package com.axlebank.budgettingmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Institution {

    private int institutionId;

    private String institutionName;

    private Long phoneNumber;

    private  String category;

    private Date createdDate;

}
