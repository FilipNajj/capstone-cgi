package com.axlebank.transactionservice.model;

import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientIdLengthException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Transaction implements Comparable<Transaction> {
    private String transactionId;
    private double amount;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateCreated = LocalDate.now();
    private TransactionType transactionType;
    private int clientId;
    private int adminId;
    private TransactionMedium transactionMedium;
    private int fromAccountNumber;
    private int toAccountNumber;
    private String emailRecipient;
    private int institutionId;
    private Role createdBy;

    public Transaction() {
    }

    public Transaction(
            double amount, TransactionType transactionType,
            int adminId, TransactionMedium transactionMedium, int fromAccountNumber,
            int toAccountNumber, String emailRecipient, int institutionId, int clientId) {
        setTransactionId(transactionType, clientId);
        setDateCreated();
        this.amount = amount;
        this.transactionType = transactionType;
        this.adminId = adminId;
        this.transactionMedium = transactionMedium;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.emailRecipient = emailRecipient;
        this.institutionId = institutionId;
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated() {
        this.dateCreated = LocalDate.now();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public TransactionMedium getTransactionMedium() {
        return transactionMedium;
    }

    public void setTransactionMedium(TransactionMedium transactionMedium) {
        this.transactionMedium = transactionMedium;
    }

    public int getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(int fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public int getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(int toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public Role getCreatedBy() {
        return createdBy;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setCreatedBy(Role createdBy) {
        this.createdBy = createdBy;
    }

    public void setTransactionId(TransactionType transactionType, int userId) throws ClientIdLengthException{
        var localDateTime = LocalDateTime.now();
        String year = String.valueOf(localDateTime.getYear()).substring(2);
        String month = String.valueOf(localDateTime.getMonthValue());
        String id;

        if (month.length() == 1){
            month = "0"+month;
        }

        try{
            id = String.valueOf(userId).substring(0,2);
        } catch (Exception e) {
            throw new ClientIdLengthException("clientId length must be min 2 digits eg. 12");
        }

        String day = String.valueOf(localDateTime.getDayOfMonth());
        int sumTime = localDateTime.getHour() + localDateTime.getMinute() + localDateTime.getSecond();

        if (transactionType.equals(TransactionType.TRANSFER)){
            sumTime += 1;
        }

        this.transactionId =  transactionType.toString().substring(0,3) + id + year + month + day + sumTime;
    }

    @Override
    public int compareTo(Transaction o) {
        return (int) (this.amount - o.amount);
    }

}

/*

[
   userTransactionId: "djshdsl",
   userId: 123,
   dateCreated: 2022-12-25
   transactionType: [
    DEPOSIT: [
        {
          "_id": "DEP122082457",
          "amount": 250.25,
          "transactionType": "DEPOSIT",
          "userId": 101,
          "transactionMedium": "CHEQUE",
          "fromAccountNumber": 0,
          "toAccountNumber": 0,
          "emailRecipient": "adrian@gmail.com",
          "institutionId": 508,
          "_class": "com.axlebank.transactionservice.model.Transaction"
        },

        {
          "_id": "DEP122082457",
          "amount": 250.25,
          "transactionType": "DEPOSIT",
          "userId": 101,
          "transactionMedium": "CHEQUE",
          "fromAccountNumber": 0,
          "toAccountNumber": 0,
          "emailRecipient": "adrian@gmail.com",
          "institutionId": 508,
          "_class": "com.axlebank.transactionservice.model.Transaction"
        }
    ]
    TRANSFER: [
     {
          "_id": "DEP122082457",
          "amount": 250.25,
          "transactionType": "TRANSFER",
          "userId": 101,
          "transactionMedium": "CHECKING",
          "fromAccountNumber": 25825,
          "toAccountNumber": 78589,
          "emailRecipient": "adrian@gmail.com",
          "institutionId": 508,
          "_class": "com.axlebank.transactionservice.model.Transaction"
        },

        {
          "_id": "DEP122082457",
          "amount": 250.25,
          "transactionType": "TRANSFER",
          "userId": 101,
          "transactionMedium": "CHECKING",
          "fromAccountNumber": 25825,
          "toAccountNumber": 45895,
          "emailRecipient": "adrian@gmail.com",
          "institutionId": 508,
          "_class": "com.axlebank.transactionservice.model.Transaction"
        }]
    PAYMENT: [ {}, {}]
    WITHDRAWAL: [ {}, {}]
    ETRANSFER: [{}, {}]

   ]


]
==== NEW TRANSACTION =======
REQUEST BODY
    TransactionType transactionType;
    TransactionMedium transactionMedium;
    int adminId; --> if admin is doing it
    double amount;
    int fromAccountNumber;
    int toAccountNumber;
    private int institutionId; ---> if payment
 */
