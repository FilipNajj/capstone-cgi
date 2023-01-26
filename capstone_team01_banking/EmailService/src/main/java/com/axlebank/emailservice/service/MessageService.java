package com.axlebank.emailservice.service;

import com.axlebank.emailservice.exception.MessageIdAlreadyExistException;
import com.axlebank.emailservice.exception.MessageIdNotFoundException;
import com.axlebank.emailservice.model.Message;

import javax.mail.MessagingException;

public interface MessageService {

    Message getMessageByMessageId(int messageId) throws MessageIdNotFoundException;
    Message addMessage(Message message) throws MessageIdAlreadyExistException;
    Message editMessage(int messageId, Message message) throws MessageIdNotFoundException;
    boolean deleteMessageById(int messageId) throws MessageIdNotFoundException;
    boolean sendMessage(Message message) throws MessagingException;

}
