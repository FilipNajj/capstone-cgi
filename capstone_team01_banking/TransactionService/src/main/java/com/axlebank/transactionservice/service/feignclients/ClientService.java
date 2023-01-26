package com.axlebank.transactionservice.service.feignclients;

import com.axlebank.transactionservice.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service", url = "http://localhost:9001/api/v1/client")
@Service
public interface ClientService {
    @GetMapping(path="/{profileId}")
    ClientDTO getClientById(@PathVariable("profileId") Integer profileId);

    @GetMapping(path="/byEmail/{clientEmail}")
    ClientDTO getClientByEmail(@PathVariable("clientEmail") String email);
}


