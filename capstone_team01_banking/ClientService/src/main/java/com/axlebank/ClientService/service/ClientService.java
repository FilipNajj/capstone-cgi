package com.axlebank.ClientService.service;

import java.util.List;

import com.axlebank.ClientService.exception.ClientAlreadyExistsException;
import com.axlebank.ClientService.exception.ClientNotFoundException;
import com.axlebank.ClientService.model.Client;

public interface ClientService {
	
	boolean addClient(Client client) throws ClientAlreadyExistsException;

	Client getClientById(Integer profileId) throws ClientNotFoundException;
	
	Client getClientByEmail(String email) throws ClientNotFoundException;
		
	Client updateClient(Client client) throws ClientNotFoundException;
	
	boolean deleteClient(Integer profileId) throws ClientNotFoundException;

	List<Client> getAllClients();
	
}



