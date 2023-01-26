package com.axlebank.transactionservice.service.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@FeignClient(name = "institution-service", url = "http://localhost:9009")
@Service
public interface InstitutionServiceClient {
    // TODO
}
