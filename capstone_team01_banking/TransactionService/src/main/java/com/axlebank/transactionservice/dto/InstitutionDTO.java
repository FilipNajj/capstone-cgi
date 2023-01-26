package com.axlebank.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {
    private Integer institutionId;
    private String institutionName;
    private long phoneNumber;
    private String category;
}
