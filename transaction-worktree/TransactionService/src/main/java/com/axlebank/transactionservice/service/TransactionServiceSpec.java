package com.axlebank.transactionservice.service;

import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.model.TransactionType;
import com.axlebank.transactionservice.dto.ClientTransactionHistoryDTO;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountNotPresentException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.TransactionNotPresentException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionServiceSpec {
   Transaction newTransaction(Transaction transactionRequested, int clientId);

   Transaction getTransactionByClientIdAndTransactionId(int clientId, String transactionId) throws ClientNotPresentException, TransactionNotPresentException;

   boolean deleteTransactionByUserIdAndTransactionId(int clientId, String transactionId);

   Map<TransactionType, List<Transaction>> getAllTransactionsMapByClientId(int clientId) throws ClientNotPresentException;

   ClientTransactionHistoryDTO getAllTransactionsByClientIdAndByAccountIdHandler(int clientId, int accountNumber) throws ClientNotPresentException, AccountNotPresentException;

   List<Transaction> getAllTransactionsListByClientId(int clientId);

   List<Transaction> getAllTransactionsByClientIdAfterDate(int clientId, LocalDate date);

   List<Transaction> getAllTransactionsByClientIdBeforeDate(int clientId, LocalDate date);
}
