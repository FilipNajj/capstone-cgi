package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.exception.MessageIdAlreadyExistException;
import com.axlebank.emailservice.exception.MessageIdNotFoundException;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.service.MessageService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable("messageId") int messageId){
        try{
            Message message = messageService.getMessageByMessageId(messageId);
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        }catch(MessageIdNotFoundException e){
            return new ResponseEntity<String>("A message with this ID does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/messages")
    @Validated
    public ResponseEntity<?> addMessageHandler(@RequestBody Message message){
        try{
            messageService.addMessage(message);
            return new ResponseEntity<Message>(message, HttpStatus.CREATED);
        }catch(MessageIdAlreadyExistException e){
            return new ResponseEntity<String>("Could not create message. A message with This ID is already present.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/messages/{messageId}")
    public ResponseEntity<?> editMessageHandler(@PathVariable("messageId") int messageId, @RequestBody Message message){
        try{
            messageService.editMessage(messageId, message);
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        }catch(MessageIdNotFoundException e){
            return new ResponseEntity<String>("A message with this ID does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessageByIdHandler(@PathVariable("messageId") int messageId){
        try{
            messageService.deleteMessageById(messageId);
            return ResponseEntity.ok("Message deleted successfully.");
        }catch(MessageIdNotFoundException e){
            return new ResponseEntity<>("A message with this ID does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/messages/send")
    public ResponseEntity<String> sendMessageHandler(@RequestBody Message message){
        try{
            messageService.sendMessage(message);
            return ResponseEntity.ok("The e-mail was sent successfully.");
        }catch(MessagingException e){
            return new ResponseEntity<>("The e-mail could not be sent.", HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> detectError(){
        return new ResponseEntity<>("The message type is not valid", HttpStatus.BAD_REQUEST);
    }

}
