package com.axlebank.emailservice.service.impl;

import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public MimeMessage buildEmtRecipientMessage(Message message) throws MessagingException {
        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> properties = setEMTProperties(message);
        context.setVariables(properties);
        helper.setFrom(message.getSenderEmail());
        helper.setTo(message.getDestinationEmail());
        helper.setSubject("Interac E-Transfer!");
        String html = templateEngine.process("EmtReceiveTemplate.html", context);
        helper.setText(html, true);
        return mail;
    }

    @Override
    public MimeMessage buildEMTconfirmationMessage(Message message) throws MessagingException {
        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> properties = setEMTProperties(message);
        context.setVariables(properties);
        helper.setFrom("axlebank@mail.com");
        helper.setTo(message.getSenderEmail());
        helper.setSubject("Interac E-Transfer confirmation!");
        String html = templateEngine.process("EmtSentTemplate.html", context);
        helper.setText(html, true);
        return mail;
    }

    @Override
    public MimeMessage buildNotificationMessage(Message message) throws MessagingException {
        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> properties = setNotificationProperties(message);
        context.setVariables(properties);
        helper.setFrom(message.getSenderEmail());
        helper.setTo(message.getDestinationEmail());
        helper.setSubject(message.getTitle());
        String html = templateEngine.process("NotificationTemplate.html", context);
        helper.setText(html, true);
        return mail;
    }

    @Override
    public Map<String, Object> setNotificationProperties(Message message) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", message.getDestinationEmail());
        properties.put("date",message.getCreatedAt());
        properties.put("content",message.getContent());
        return properties;
    }

    @Override
    public Map<String, Object> setEMTProperties(Message message) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", message.getDestinationEmail());
        properties.put("from",message.getSenderEmail());
        properties.put("amount",message.getAmount());
        return properties;
    }
}
