package com.axlebank.transactionservice.service.feignclients;

import com.axlebank.transactionservice.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "http://localhost:9005/api/v1/accounts")
@Service
public interface AccountManagementServiceClient {

    @GetMapping("/{accountNumber}")
    AccountDTO getAccountByIds(@PathVariable int accountNumber);

    @PutMapping("/{accountNumber}")
    AccountDTO updateAccount(@RequestBody AccountDTO updatedAccountDTO, @PathVariable int accountNumber);
}
