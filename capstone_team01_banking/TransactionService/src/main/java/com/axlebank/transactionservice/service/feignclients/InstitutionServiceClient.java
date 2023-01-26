package com.axlebank.transactionservice.service.feignclients;

import com.axlebank.transactionservice.dto.InstitutionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "institution-service", url = "http://localhost:9009/api/v1/institution")
@Service
public interface InstitutionServiceClient {
    @GetMapping("/findById/{id}")
    InstitutionDTO findInstitutionById(@PathVariable Integer id);
}
