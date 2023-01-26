package com.axlebank.emailservice.service;

import com.axlebank.emailservice.dto.EmtDTO;

import java.util.List;

public interface EtransferService {
    List<EmtDTO> getAllEtransferSentTo(String destinationEmail);
    List<EmtDTO> getAllEtransferSentFrom(String senderEmail);

}
