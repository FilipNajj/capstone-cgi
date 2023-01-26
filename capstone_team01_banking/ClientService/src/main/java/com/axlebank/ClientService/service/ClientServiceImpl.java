package com.axlebank.ClientService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.axlebank.ClientService.exception.ClientAlreadyExistsException;
import com.axlebank.ClientService.exception.ClientNotFoundException;
import com.axlebank.ClientService.model.Client;
import com.axlebank.ClientService.repository.ClientServiceRepository;

@Service
public class ClientServiceImpl implements ClientService{
	
	@Autowired
	private ClientServiceRepository clientRepo;
	
	@Override
	public boolean addClient(Client client) throws ClientAlreadyExistsException {
		Optional<Client> optional =  clientRepo.findById(client.getProfileId());
		if(optional.isPresent()) {
			throw new ClientAlreadyExistsException();
		}
		clientRepo.save(client);
		return true;
	}

	@Override
	public Client getClientById(Integer profileId) throws ClientNotFoundException {
		Optional<Client> optional = clientRepo.findById(profileId);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new ClientNotFoundException();
	}
	
	@Override
	public Client updateClient(Client client) throws ClientNotFoundException {
		Optional<Client> optional =  clientRepo.findById(client.getProfileId());
		if(optional.isPresent()) {
			clientRepo.save(client);
			return client;
		}
		throw new ClientNotFoundException();
	}
	
	@Override
	public boolean deleteClient(Integer profileId) throws ClientNotFoundException {
		Optional<Client> optional =  clientRepo.findById(profileId);
		if(optional.isPresent()) {
			clientRepo.delete(optional.get());
			return true;
		}
		throw new ClientNotFoundException();
	}

	@Override
	public List<Client> getAllClients() {
		return clientRepo.findAll();
	}

	@Override
	public Client getClientByEmail(String email) throws ClientNotFoundException {
		Optional<Client> optional = clientRepo.findByEmail(email);
		if (optional.isPresent()) {
			System.out.println(optional.get());
			return optional.get();
		}
		throw new ClientNotFoundException();
	}

}

