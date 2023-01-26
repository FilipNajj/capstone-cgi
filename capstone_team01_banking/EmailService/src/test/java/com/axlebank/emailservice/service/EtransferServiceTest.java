package com.axlebank.emailservice.service;

import com.axlebank.emailservice.repository.MessageRepository;
import com.axlebank.emailservice.service.impl.EtransferServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class EtransferServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private EtransferServiceImpl etransferService;

    @Test
    public void whenGetAllEtransferSentToShouldReturnEtransfers(){

    }

}
