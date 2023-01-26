package com.axlebank.emailservice.service.impl;

import com.axlebank.emailservice.exception.MessageIdAlreadyExistException;
import com.axlebank.emailservice.exception.MessageIdNotFoundException;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import com.axlebank.emailservice.repository.MessageRepository;
import com.axlebank.emailservice.service.EmailService;
import com.axlebank.emailservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public Message getMessageByMessageId(int messageId) throws MessageIdNotFoundException {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        throw new MessageIdNotFoundException();
    }

    @Override
    public Message addMessage(Message message) throws MessageIdAlreadyExistException {
        Optional<Message> optionalMessage = messageRepository.findById(message.getMessageId());
        if(optionalMessage.isEmpty()){
            messageRepository.save(message);
            return message;
        }
        throw new MessageIdAlreadyExistException();
    }

    @Override
    public Message editMessage(int messageId, Message message) throws MessageIdNotFoundException {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            messageRepository.save(message);
            return message;
        }
        throw new MessageIdNotFoundException();
    }

    @Override
    public boolean deleteMessageById(int messageId) throws MessageIdNotFoundException {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            messageRepository.deleteById(messageId);
            return true;
        }
        throw new MessageIdNotFoundException();
    }



    @Override
    public boolean sendMessage(Message message) throws MessagingException {
        try{
            if(message.getMessageType().equals(MessageTypeEnum.EMT)){
                MimeMessage email = emailService.buildEmtRecipientMessage(message);
                emailSender.send(email);
                messageRepository.save(message);
                email = emailService.buildEMTconfirmationMessage(message);
                emailSender.send(email);
            } else if(message.getMessageType().equals(MessageTypeEnum.MAIL)){
                MimeMessage email = emailService.buildNotificationMessage(message);
                emailSender.send(email);
                messageRepository.save(message);
            }
        }catch(MessagingException e){
            throw new MessagingException();
        }
        return true;
    }
}
