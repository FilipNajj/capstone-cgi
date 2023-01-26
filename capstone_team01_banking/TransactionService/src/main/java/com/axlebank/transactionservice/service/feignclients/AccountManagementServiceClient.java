package com.axlebank.transactionservice.service.feignclients;

import com.axlebank.transactionservice.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "account-service", url = "http://localhost:9005/api/v1/accounts")
@Service
public interface AccountManagementServiceClient {

    @GetMapping("/accounts-profile/{profileId}")
    List<AccountDTO> getAccountByProfileId(@PathVariable int profileId);

    @GetMapping("/{accountNumber}")
    AccountDTO getAccountByIds(@PathVariable int accountNumber);

    @PutMapping("/{accountNumber}")
    AccountDTO updateAccount(@RequestBody AccountDTO updatedAccountDTO, @PathVariable int accountNumber);
}


