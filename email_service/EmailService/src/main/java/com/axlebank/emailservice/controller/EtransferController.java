package com.axlebank.emailservice.controller;

import com.axlebank.emailservice.dto.EmtDTO;
import com.axlebank.emailservice.service.EtransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EtransferController {

    @Autowired
    private EtransferService emtService;

    @GetMapping("/etransfers/to/{destinationEmail}")
    public ResponseEntity<List<EmtDTO>> getAllEtransfersSentToHandler(@PathVariable("destinationEmail") String destinationEmail){
        List<EmtDTO> emtList = emtService.getAllEtransferSentTo(destinationEmail);
        return ResponseEntity.ok(emtList);
    }

    @GetMapping("/etransfers/from/{senderEmail}")
    public ResponseEntity<List<EmtDTO>> getAllEtransfersSentFromHandler(@PathVariable("senderEmail") String senderEmail){
        List<EmtDTO> emtList = emtService.getAllEtransferSentFrom(senderEmail);
        return ResponseEntity.ok(emtList);
    }

}
