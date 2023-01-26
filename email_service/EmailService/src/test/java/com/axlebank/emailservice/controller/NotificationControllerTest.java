package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.dto.MailDTO;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import com.axlebank.emailservice.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationServiceImpl notificationService;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void GivenGetAllMessagesSentToRequestThenResponseShouldContainAllMessagesSentTo() throws Exception {
        Message message1 = new Message(1,"sender@a.com", "destination@b.com",
                "Hello", "Content", LocalDateTime.now(), MessageTypeEnum.MAIL,0.00);
        Message message2 = new Message(2,"sender@a.com", "destination@b.com",
                "Hi", "Content", LocalDateTime.now(), MessageTypeEnum.EMT,50.00);
        List<Message> messageList = List.of(message1, message2);
        List<Message> result = messageList.stream().filter(m-> m.getMessageType() == MessageTypeEnum.MAIL).collect(Collectors.toList());
        List<MailDTO> mailDTOS = new ArrayList<>();
        for(Message m: result){
            ModelMapper mapper = new ModelMapper();
            MailDTO mailDTO = mapper.map(m, MailDTO.class);
            mailDTOS.add(mailDTO);
        }
        Mockito.when(notificationService.getAllNotificationsSentTo("destination@b.com")).thenReturn(mailDTOS);
        ResponseEntity<List<MailDTO>> responseEntity = notificationController.getAllMessagesSentToHandler("destination@b.com");
        List<MailDTO> list = responseEntity.getBody();
        assertEquals(1, list.size() );
        assertEquals(list, mailDTOS);
        assertEquals("destination@b.com",list.get(0).getDestinationEmail());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void GivenGetAllMessagesSentFromRequestThenResponseShouldContainAllMessagesSentFrom() throws Exception {
        Message message1 = new Message(1,"sender@a.com", "destination@b.com",
                "Hello", "Content", LocalDateTime.now(), MessageTypeEnum.MAIL,0.00);
        Message message2 = new Message(2,"sender@a.com", "destination@b.com",
                "Hi", "Content", LocalDateTime.now(), MessageTypeEnum.EMT,50.00);
        List<Message> messageList = List.of(message1, message2);
        List<Message> result = messageList.stream().filter(m-> m.getMessageType() == MessageTypeEnum.MAIL).collect(Collectors.toList());
        List<MailDTO> mailDTOS = new ArrayList<>();
        for(Message m: result){
            ModelMapper mapper = new ModelMapper();
            MailDTO mailDTO = mapper.map(m, MailDTO.class);
            mailDTOS.add(mailDTO);
        }
        Mockito.when(notificationService.getAllNotificationsSentFrom("sender@a.com")).thenReturn(mailDTOS);
        ResponseEntity<List<MailDTO>> responseEntity = notificationController.getAllMessagesSentFromHandler("sender@a.com");
        List<MailDTO> list = responseEntity.getBody();
        assertEquals(1, list.size() );
        assertEquals(list, mailDTOS);
        assertEquals("sender@a.com",list.get(0).getSenderEmail());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }



}
