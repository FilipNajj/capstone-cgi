package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.exception.MessageIdAlreadyExistException;
import com.axlebank.emailservice.exception.MessageIdNotFoundException;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import com.axlebank.emailservice.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageServiceImpl messageService;


    final String NO_ID_MSG = "A message with this ID does not exist.";
    final String ID_PRESENT_MSG = "Could not create message. A message with This ID is already present.";
    private Message message;
    private List<Message> messageList;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        Message message1 = new Message(1,"sender@a.com", "destination@b.com",
                "Hello", "Content", LocalDateTime.now(), MessageTypeEnum.MAIL,0.00);
        Message message2 = new Message(2,"sender@a.com", "destination@b.com",
                "Hi", "Content", LocalDateTime.now(), MessageTypeEnum.EMT,50.00);
        messageList = Arrays.asList(message1, message2);
    }

    @Test
    void GivenValidMessageIdReturnAppropriateMessage() throws MessageIdNotFoundException {
        Message message = messageList.stream().filter(m-> m.getMessageId() == 1).findFirst().get();
        Mockito.when(messageService.getMessageByMessageId(1)).thenReturn(message);
        ResponseEntity<?> responseEntity = messageController.getMessageById(1);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Message messageBody = (Message)responseEntity.getBody();
        assertEquals(1, messageBody.getMessageId());
    }

    @Test
    void GivenInvalidMessageIdReturnAppropriateMessage() throws MessageIdNotFoundException {
        Mockito.when(messageService.getMessageByMessageId(3)).thenThrow(MessageIdNotFoundException.class);
        ResponseEntity<?> responseEntity = messageController.getMessageById(3);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(NO_ID_MSG, responseEntity.getBody());
    }

    @Test
    void testAddMessageGivenValidMessage() throws MessageIdAlreadyExistException {
        Mockito.when(messageService.addMessage(messageList.get(0))).thenReturn(messageList.get(0));
        ResponseEntity<?> responseEntity = messageController.addMessageHandler(messageList.get(0));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody(), messageList.get(0));
    }

    @Test
    void testAddMessageGivenInvalidMessageId() throws MessageIdAlreadyExistException{
        Mockito.when(messageService.addMessage(null)).thenThrow(MessageIdAlreadyExistException.class);
        ResponseEntity<?> responseEntity = messageController.addMessageHandler(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ID_PRESENT_MSG, responseEntity.getBody());
    }

    @Test
    void testEditMessageWithValidMessageId() throws MessageIdNotFoundException{
        Message message = messageList.get(0);
        message.setContent("newContent");
        message.setTitle("newTitle");
        Mockito.when(messageService.editMessage(1,message)).thenReturn(message);
        ResponseEntity<?> responseEntity = messageController.editMessageHandler(1, message);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), message);
    }

    @Test
    void testEditMessageWithInvalidMessageId() throws MessageIdNotFoundException{
        Mockito.when(messageService.editMessage(12,messageList.get(0))).thenThrow(MessageIdNotFoundException.class);
        ResponseEntity<?> responseEntity = messageController.editMessageHandler(12, messageList.get(0));
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(NO_ID_MSG, responseEntity.getBody());
    }

    @Test
    void testDeleteMessageGivenValidMessage() throws MessageIdNotFoundException{
        Mockito.when(messageService.deleteMessageById(1)).thenReturn(true);
        ResponseEntity<?> responseEntity = messageController.deleteMessageByIdHandler(1);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Message deleted successfully.", responseEntity.getBody());
    }

    @Test
    void testDeleteMessageGivenInvalidMessage() throws MessageIdNotFoundException{
        Mockito.when(messageService.deleteMessageById(10)).thenThrow(MessageIdNotFoundException.class);
        ResponseEntity<?> responseEntity = messageController.deleteMessageByIdHandler(10);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(NO_ID_MSG, responseEntity.getBody());
    }

    @Test
    void testSendMessageSuccess() throws MessagingException{
        Mockito.when(messageService.sendMessage(message)).thenReturn(true);
        ResponseEntity<String> responseEntity = messageController.sendMessageHandler(message);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The e-mail was sent successfully.", responseEntity.getBody());
    }

    @Test
    void testSendMessageFailure() throws MessagingException{
        Mockito.when(messageService.sendMessage(message)).thenThrow(MessagingException.class);
        ResponseEntity<String> responseEntity = messageController.sendMessageHandler(message);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("The e-mail could not be sent.", responseEntity.getBody());
    }
}
