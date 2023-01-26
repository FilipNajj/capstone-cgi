package com.axlebank.transactionservice.repository;

import com.axlebank.transactionservice.model.UserTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientTransactionRepository extends MongoRepository<UserTransaction, UUID> {
    Optional<UserTransaction> findByUserClientId(int userClientId);
}
