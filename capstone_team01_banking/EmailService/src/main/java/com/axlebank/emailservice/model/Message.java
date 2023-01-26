package com.axlebank.emailservice.model;

import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Message {

    @Id
    private int messageId = new Random().nextInt();
    private String senderName;
    private String senderEmail;
    private String recipientName;
    private String destinationEmail;
    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private MessageTypeEnum messageType;
    private double amount;


}
