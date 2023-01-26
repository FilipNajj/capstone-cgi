package com.axlebank.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EtranferDTO {
    private String senderName;
    private String senderEmail;
    private String recipientName;
    private String destinationEmail;
    private String messageType = "EMT";
    private double amount;
}
