package com.axlebank.ClientService.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.ClientService.model.Client;
@Repository
public interface ClientServiceRepository extends MongoRepository<Client, Integer>{

	Optional<Client> findByEmail(String email);


	
}
