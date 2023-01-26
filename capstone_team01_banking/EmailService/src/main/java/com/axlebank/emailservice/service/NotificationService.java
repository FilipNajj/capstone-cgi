package com.axlebank.emailservice.service;

import com.axlebank.emailservice.dto.MailDTO;

import java.util.List;

public interface NotificationService {
    List<MailDTO> getAllNotificationsSentTo(String destinationEmail);
    List<MailDTO> getAllNotificationsSentFrom(String senderEmail);

}
