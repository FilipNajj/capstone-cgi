package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.dto.MailDTO;
import com.axlebank.emailservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications/to/{destinationEmail}")
    public ResponseEntity<List<MailDTO>> getAllMessagesSentToHandler(@PathVariable("destinationEmail") String destinationEmail){
        List<MailDTO> messageList = notificationService.getAllNotificationsSentTo(destinationEmail);
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/notifications/from/{senderEmail}")
    public ResponseEntity<List<MailDTO>> getAllMessagesSentFromHandler(@PathVariable("senderEmail") String senderEmail){
        List<MailDTO> messageList = notificationService.getAllNotificationsSentFrom(senderEmail);
        return ResponseEntity.ok(messageList);
    }

}
