package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.dto.EmtDTO;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.model.enums.MessageTypeEnum;
import com.axlebank.emailservice.service.impl.EtransferServiceImpl;
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
public class EtransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EtransferController etransferController;

    @Mock
    private EtransferServiceImpl etransferService;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(etransferController).build();
    }

    @Test
    void GivenGetAllEmtSentToRequestThenResponseShouldContainAllEmtSentTo() throws Exception {
        Message message1 = new Message(1,"sender@a.com", "destination@b.com",
                "Hello", "Content", LocalDateTime.now(), MessageTypeEnum.MAIL,0.00);
        Message message2 = new Message(2,"sender@a.com", "destination@b.com",
                "Hi", "Content", LocalDateTime.now(), MessageTypeEnum.EMT,50.00);
        List<Message> messageList = List.of(message1, message2);
        List<Message> result = messageList.stream().filter(m-> m.getMessageType() == MessageTypeEnum.MAIL).collect(Collectors.toList());
        List<EmtDTO> emtDTOS = new ArrayList<>();
        for(Message m: result){
            ModelMapper mapper = new ModelMapper();
            EmtDTO emtDTO = mapper.map(m, EmtDTO.class);
            emtDTOS.add(emtDTO);
        }
        Mockito.when(etransferService.getAllEtransferSentTo("destination@b.com")).thenReturn(emtDTOS);
        ResponseEntity<List<EmtDTO>> responseEntity = etransferController.getAllEtransfersSentToHandler("destination@b.com");
        List<EmtDTO> list = responseEntity.getBody();
        assertEquals(1, list.size() );
        assertEquals(list, emtDTOS);
        assertEquals("destination@b.com",list.get(0).getDestinationEmail());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void GivenGetAllEmtSentFromRequestThenResponseShouldContainAllEmtSentFrom() throws Exception {
        Message message1 = new Message(1,"sender@a.com", "destination@b.com",
                "Hello", "Content", LocalDateTime.now(), MessageTypeEnum.MAIL,0.00);
        Message message2 = new Message(2,"sender@a.com", "destination@b.com",
                "Hi", "Content", LocalDateTime.now(), MessageTypeEnum.EMT,50.00);
        List<Message> messageList = List.of(message1, message2);
        List<Message> result = messageList.stream().filter(m-> m.getMessageType() == MessageTypeEnum.MAIL).collect(Collectors.toList());
        List<EmtDTO> emtDTOS = new ArrayList<>();
        for(Message m: result){
            ModelMapper mapper = new ModelMapper();
            EmtDTO emtDTO = mapper.map(m, EmtDTO.class);
            emtDTOS.add(emtDTO);
        }
        Mockito.when(etransferService.getAllEtransferSentFrom("sender@a.com")).thenReturn(emtDTOS);
        ResponseEntity<List<EmtDTO>> responseEntity = etransferController.getAllEtransfersSentFromHandler("sender@a.com");
        List<EmtDTO> list = responseEntity.getBody();
        assertEquals(1, list.size() );
        assertEquals(list, emtDTOS);
        assertEquals("sender@a.com",list.get(0).getSenderEmail());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
