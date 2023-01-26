package com.axlebank.emailservice.dto;

import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MailDTO {

    private int messageId;
    private String senderEmail;
    private String destinationEmail;
    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private MessageTypeEnum messageType;

}
