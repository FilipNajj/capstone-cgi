package com.axlebank.transactionservice.service.feignclients;

import com.axlebank.transactionservice.dto.EtranferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "http://localhost:9006")
@Service
public interface EmailServiceClient {
    @PostMapping("/api/v1/messages/send")
    String sendEtransfer(@RequestBody EtranferDTO etranferDTO);
}
