package com.axlebank.emailservice.service.impl;

import com.axlebank.emailservice.dto.EmtDTO;
import com.axlebank.emailservice.model.Message;
import com.axlebank.emailservice.repository.MessageRepository;
import com.axlebank.emailservice.service.EtransferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EtransferServiceImpl implements EtransferService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<EmtDTO> getAllEtransferSentTo(String destinationEmail) {
        List<Message> messages = messageRepository.findMessagesByDestinationEmailAndMessageType(destinationEmail, "EMT");
        List<EmtDTO> emtDTOS = new ArrayList<>();
        if(!messages.isEmpty()){
            for(Message m : messages){
                ModelMapper mapper = new ModelMapper();
                EmtDTO emtDTO = mapper.map(m, EmtDTO.class);
                emtDTOS.add(emtDTO);
            }
        }
        return emtDTOS;
    }

    @Override
    public List<EmtDTO> getAllEtransferSentFrom(String senderEmail) {
        List<Message> messages = messageRepository.findMessagesBySenderEmailAndMessageType(senderEmail, "EMT");
        List<EmtDTO> emtDTOS = new ArrayList<>();
        if(!messages.isEmpty()){
            for(Message m : messages){
                ModelMapper mapper = new ModelMapper();
                EmtDTO emtDTO = mapper.map(m, EmtDTO.class);
                emtDTOS.add(emtDTO);
            }
        }
        return emtDTOS;
    }

}
