package com.axlebank.emailservice.repository;

import com.axlebank.emailservice.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Integer> {
    List<Message> findMessagesBySenderEmailAndMessageType(String senderEmail, String type);
    List<Message> findMessagesByDestinationEmailAndMessageType(String destinationEmail, String type);

}
