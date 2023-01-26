package com.axlebank.transactionservice.service.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@FeignClient(name = "email-service", url = "http://localhost:9006")
@Service
public interface EmailServiceClient {
    // TODO
    // notify email service to send email for etransfer
}
