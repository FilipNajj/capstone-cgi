package com.axlebank.emailservice.service;

import com.axlebank.emailservice.model.Message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

public interface EmailService {

    MimeMessage buildEmtRecipientMessage(Message message) throws MessagingException;
    MimeMessage buildEMTconfirmationMessage(Message message) throws MessagingException;
    MimeMessage buildNotificationMessage(Message message) throws MessagingException;
    Map<String, Object> setNotificationProperties(Message message);
    Map<String, Object> setEMTProperties(Message message);

}
