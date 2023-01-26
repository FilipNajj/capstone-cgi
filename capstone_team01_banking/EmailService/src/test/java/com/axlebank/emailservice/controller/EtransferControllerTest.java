package com.axlebank.emailservice.controller;
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

@ExtendWith(MockitoExtension.class)
public class EtransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EtransferController etransferController;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(etransferController).build();
    }

}
