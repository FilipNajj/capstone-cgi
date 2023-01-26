package com.axlebank.FeignClient;

import com.axlebank.model.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@FeignClient(value="client-service", url="http://localhost:9001/api/v1/client")
public interface FeignService {

	//UPDATE CLIENT WHEN UPDATING ACCOUNT
	@PutMapping("/{profileId}")
	public Client updateClient(
			@PathVariable Integer profileId, @RequestBody Client updatedClient);
	
	@GetMapping(path="/{profileId}")
	public Client getClientById(@PathVariable Integer profileId);

}
