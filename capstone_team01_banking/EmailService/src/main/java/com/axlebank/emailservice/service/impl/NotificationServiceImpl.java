package com.axlebank.emailservice.service.impl;

import com.axlebank.emailservice.dto.MailDTO;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.repository.MessageRepository;
import com.axlebank.emailservice.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<MailDTO> getAllNotificationsSentTo(String destinationEmail) {
        List<Message> messages = messageRepository.findMessagesByDestinationEmailAndMessageType(destinationEmail, "MAIL");
        List<MailDTO> mailDTOS = new ArrayList<>();
        if (!messages.isEmpty()) {
            for (Message m : messages) {
                ModelMapper mapper = new ModelMapper();
                MailDTO mailDTO = mapper.map(m, MailDTO.class);
                mailDTOS.add(mailDTO);
            }
        }
        return mailDTOS;
    }

    @Override
    public List<MailDTO> getAllNotificationsSentFrom(String senderEmail) {
        List<Message> messages = messageRepository.findMessagesBySenderEmailAndMessageType(senderEmail, "MAIL");
        List<MailDTO> mailDTOS = new ArrayList<>();
        if (!messages.isEmpty()) {
            for (Message m : messages) {
                ModelMapper mapper = new ModelMapper();
                MailDTO mailDTO = mapper.map(m, MailDTO.class);
                mailDTOS.add(mailDTO);
            }
        }
        return mailDTOS;
    }
}
