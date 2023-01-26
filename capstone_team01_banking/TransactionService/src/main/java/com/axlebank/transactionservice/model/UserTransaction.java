package com.axlebank.transactionservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.IdGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_transactions")
@Data
public class UserTransaction implements Serializable, Comparable<UserTransaction>, IdGenerator {
    @Id
    private UUID userTransactionId;
    private int userClientId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateCreated = LocalDate.now();
    private TransactionType transactionType;
    private Map<TransactionType, List<Transaction>> transactions;


    public UserTransaction(int userClientId) {
        this.transactions = new TreeMap<>();
        this.transactions.put(TransactionType.DEPOSIT, new ArrayList<>());
        this.transactions.put(TransactionType.WITHDRAWAL, new ArrayList<>());
        this.transactions.put(TransactionType.PAYMENT, new ArrayList<>());
        this.transactions.put(TransactionType.TRANSFER, new ArrayList<>());
        this.transactions.put(TransactionType.ETRANSFER, new ArrayList<>());
        this.userClientId = userClientId;
    }

    public void setUserTransactionId() {
        this.userTransactionId = generateId();
    }


    @Override
    public int compareTo(UserTransaction o) {
        return this.dateCreated.compareTo(o.dateCreated);
    }

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}

