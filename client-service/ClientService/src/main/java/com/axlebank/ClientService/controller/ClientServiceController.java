package com.axlebank.ClientService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axlebank.ClientService.exception.ClientAlreadyExistsException;
import com.axlebank.ClientService.exception.ClientNotFoundException;
import com.axlebank.ClientService.model.Client;
import com.axlebank.ClientService.service.ClientService;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping(path="/api/v1/client")
public class ClientServiceController {
	
	/*
	 * ​- /api/v1/client - POST Profile profile (Register)
	 * - /api/v1/client/{id} - Delete a client with id = id
	 * - /api/v1/client/{id} - GET - get client with id = id
	 * - /api/v1/client/{id} - PUT Profile profile (update a client)
	 * - /api/v1/client - GET - get  all clients
	 */
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/home")
	public Map<String, String> homeHandler() {
		Map<String, String> map = Map.of("message","Welcome to stackroute");
		return map;
	}

	
	@PostMapping("")
	public ResponseEntity<Client> createClient(@RequestBody Client client) throws ClientAlreadyExistsException{ 
		Boolean isClientAdded = clientService.addClient(client);
		if(isClientAdded == true) {
			return new ResponseEntity<Client>(client, HttpStatus.CREATED);
		}
		return new ResponseEntity<Client>(HttpStatus.CONFLICT);
	}
		
	@DeleteMapping("/{profileId}")
	public ResponseEntity<Client> removeClient(@PathVariable("profileId") Integer profileId) throws ClientNotFoundException{
		boolean clientServiceDeleted = clientService.deleteClient(profileId);
		if(clientServiceDeleted == true) {
			return new ResponseEntity<Client>(HttpStatus.OK);
		}
		return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(path="/{profileId}")
	public ResponseEntity<Client> getClientById(@PathVariable("profileId") Integer profileId){
		ResponseEntity<Client> responseEntity;
		try {
			Client client = clientService.getClientById(profileId);
			responseEntity = new ResponseEntity<Client>(client,HttpStatus.OK);
		} catch (ClientNotFoundException e) {
			responseEntity = new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	
	@GetMapping(path="/byEmail/{email}")
	public ResponseEntity<Client> getClientByEmail(@PathVariable("email") String email){
		ResponseEntity<Client> responseEntity;
		try {
			Client client = clientService.getClientByEmail(email);
			responseEntity = new ResponseEntity<Client>(client,HttpStatus.OK);
		} catch (ClientNotFoundException e) {
			responseEntity = new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	
	
	@PutMapping("/{profileId}")
	public ResponseEntity<Client> updateClient(@PathVariable Integer profileId, @RequestBody Client updatedClient){
		ResponseEntity<Client> responseEntity;
		try {
			updatedClient.setProfileId(profileId);
			Client client = clientService.updateClient(updatedClient);
			responseEntity = new ResponseEntity<Client>(client,HttpStatus.OK);
		}catch (ClientNotFoundException e) {
			responseEntity = new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	
	@GetMapping("")
	public ResponseEntity<List<Client>> getAllClients(){
		List<Client> clients = clientService.getAllClients();
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}	

}
