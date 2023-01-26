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
            id = String.valueOf(userId).substring(0,1);
        } catch (Exception e) {
            throw new ClientIdLengthException("clientId length must be min 1 digits eg. 1");
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

