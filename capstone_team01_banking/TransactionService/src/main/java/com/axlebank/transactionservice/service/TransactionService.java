package com.axlebank.transactionservice.service;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.*;
import com.axlebank.transactionservice.repository.ClientTransactionRepository;
import com.axlebank.transactionservice.service.depositservice.DepositServiceSpec;
import com.axlebank.transactionservice.service.etransferservice.EtransferServiceSpec;
import com.axlebank.transactionservice.service.feignclients.AccountManagementServiceClient;
import com.axlebank.transactionservice.service.paymentservice.PaymentService;
import com.axlebank.transactionservice.service.transferservice.TransferService;
import com.axlebank.transactionservice.service.withdrawalservice.WithdrawalService;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.*;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionServiceSpec{

    private final ClientTransactionRepository clientTransactionRepository;

    private final DepositServiceSpec depositService;
    private final WithdrawalService withdrawalService;
    private final PaymentService paymentService;
    private final TransferService transferService;

    private final EtransferServiceSpec etransferService;
    private final AccountManagementServiceClient accountManagementService;

    public TransactionService(ClientTransactionRepository clientTransactionRepository, DepositServiceSpec depositService, WithdrawalService withdrawalService, PaymentService paymentService, TransferService transferService, EtransferServiceSpec etransferService, AccountManagementServiceClient accountManagementService) {
        this.clientTransactionRepository = clientTransactionRepository;
        this.depositService = depositService;
        this.withdrawalService = withdrawalService;
        this.paymentService = paymentService;
        this.transferService = transferService;
        this.etransferService = etransferService;
        this.accountManagementService = accountManagementService;
    }


    // ======================================== Add new Transaction starts here =================================================================
    @Override
    public Transaction newTransaction(Transaction transactionRequested, int clientId) {
            List<Transaction> transactionsProcessedList = new ArrayList<>();
            var transactionType = transactionRequested.getTransactionType();

            switch (transactionType){
                case DEPOSIT: {
                    var list =depositService.makeDeposit(transactionRequested, clientId);
                    if (list.size() == 0){
                        throw new TransactionFailedException("Failed to DEPOSIT, failed to update - server under under testing,  please try later");
                    }else {
                        transactionsProcessedList.addAll(list);
                    }
                }
                break;

                case WITHDRAWAL: {
                    var list =withdrawalService.makeWithdrawal(transactionRequested, clientId);
                    if (list.size() == 0){
                        throw new TransactionFailedException("Failed to WITHDRAWAL, failed to update - server under under testing,  please try later");
                    }else {
                        transactionsProcessedList.addAll(list);
                    }
                }
                break;

                case PAYMENT:{
                    var list =paymentService.makePayment(transactionRequested, clientId);
                    if (list.size() == 0){
                        throw new TransactionFailedException("Failed to PAYMENT, failed to update - server under under testing,  please try later");
                    }else {
                        transactionsProcessedList.addAll(list);
                    }
                }
                break;

                case TRANSFER: {
                    var list = transferService.makeTransfer(transactionRequested, clientId);
                    if (list.size() == 0){
                        throw new TransactionFailedException("Failed to process" + transactionType + ", please try later");
                    }else {
                        transactionsProcessedList.addAll(list);
                    }
                }
                break;

                case ETRANSFER: {
                    var list = etransferService.makeETransfer(transactionRequested, clientId);
                    if (list.size() == 0){
                        throw new TransactionFailedException("Failed to process" + transactionType + ", please try later");
                    }else {
                        transactionsProcessedList.addAll(list);
                    }
                }
                break;

                default:
                    throw new TransactionTypeNotPresent("Transaction requested is not available.");

            }

        for (Transaction t: transactionsProcessedList ) {
            Optional<UserTransaction> userTransactionOptional
                    = clientTransactionRepository.findByUserClientId(t.getClientId());

            if (userTransactionOptional.isEmpty()){
                var userTransaction = new UserTransaction(t.getClientId());
                userTransaction.setUserTransactionId();

                userTransaction
                        .getTransactions()
                        .get(t.getTransactionType())
                        .add(t);

                clientTransactionRepository.save(userTransaction);
            }else {
                userTransactionOptional.get()
                        .getTransactions()
                        .get(t.getTransactionType())
                        .add(t);

                clientTransactionRepository.save(userTransactionOptional.get());
            }
        }

        if (transactionsProcessedList.size() > 0){
            transactionRequested = transactionsProcessedList.get(0);
        }

        transactionsProcessedList.clear();

        return transactionRequested;

    }

// ======================================== Add new Transaction ends here =================================================================

    @Override
    public Transaction getTransactionByClientIdAndTransactionId(
            int clientId, String transactionId) throws ClientNotPresentException {
        var optionalUserTransaction = clientTransactionRepository.findByUserClientId(clientId);

        if (optionalUserTransaction.isEmpty()){
            throw new ClientNotPresentException("Client with id " + clientId +  " does not have any transaction");
        }

        var map = optionalUserTransaction.get().getTransactions();
        TransactionType transactionType = getTransactionTypeByTransactionId(transactionId);
        var optionalTransaction  = getTransactionByIdAndType(transactionId, map, transactionType);
        if (optionalTransaction.isEmpty()){
            throw new TransactionNotPresentException();
        }
        return optionalTransaction.get();
    }

    @Override
    public Map<TransactionType, List<Transaction>> getAllTransactionsMapByClientId(int clientId) throws ClientNotPresentException {
        Optional<UserTransaction> optionalUserTransactions = clientTransactionRepository.findByUserClientId(clientId);

        if (optionalUserTransactions.isEmpty()){
            throw new ClientNotPresentException("Client with id " + clientId +  " does not have any transaction");
        }
        return optionalUserTransactions.get().getTransactions();
    }

    @Override
    public ClientTransactionHistoryDTO getAllTransactionsByClientIdAndByAccountIdHandler(int clientId, int accountNumber) throws ClientNotPresentException, AccountNotPresentException {
        var allTransactionsByClientId = getAllTransactionsMapByClientId(clientId);
        var transactionTypes = TransactionType.values();
        List<Transaction> transactionListByUserIdAndAccountId = new ArrayList<>();

        for (TransactionType type:transactionTypes) {
             var searchResultFromAccount = allTransactionsByClientId.get(type)
                     .stream()
                     .filter(
                             transaction -> (transaction.getFromAccountNumber() == accountNumber && doesAccountBelongToUser(clientId, transaction.getFromAccountNumber(), transaction)))
                     .collect(Collectors.toList());

            var searchResultToAccount = allTransactionsByClientId.get(type)
                    .stream()
                    .filter(
                            transaction -> (transaction.getToAccountNumber() == accountNumber && doesAccountBelongToUser(clientId, transaction.getToAccountNumber(), transaction)))
                    .collect(Collectors.toList());

            transactionListByUserIdAndAccountId.addAll(searchResultToAccount);
            searchResultToAccount.clear();

             transactionListByUserIdAndAccountId.addAll(searchResultFromAccount);
            searchResultFromAccount.clear();
       }

       if (transactionListByUserIdAndAccountId.size() == 0){
           throw new AccountNotPresentException("No transactions with accountNo " + accountNumber + " for user " + clientId);
       }

       var userTransactionHistoryByAccountIdDTO =  new ClientTransactionHistoryDTO();
       userTransactionHistoryByAccountIdDTO.setTransactionHistory(accountNumber, transactionListByUserIdAndAccountId);
       return userTransactionHistoryByAccountIdDTO;
    }

    private boolean doesAccountBelongToUser(int clientId, int account, Transaction transaction){
        var accountOptional
                = getAccountDetailsInAccountManagementService(clientId, transaction, account);

        if (accountOptional.isEmpty()){
            throw new AccountNotPresentException("Account is not present");
        }

        return account == accountOptional.get().getAccountNumber();
    }

    @Override
    public List<Transaction> getAllTransactionsListByClientId(int clientId) {
        var allTransactionsByClientId = getAllTransactionsMapByClientId(clientId);
        var transactionTypes = TransactionType.values();
        List<Transaction> transactionListByUserId = new ArrayList<>();

        for (TransactionType type:transactionTypes) {
            var searchResult = new ArrayList<>(allTransactionsByClientId.get(type));
            transactionListByUserId.addAll(searchResult);
            searchResult.clear();
        }
        return transactionListByUserId;
    }

    @Override
    public List<Transaction> getAllTransactionsByClientIdAfterDate(int clientId, LocalDate date) {
        var clientTransactionList= getAllTransactionsListByClientId(clientId);

        return
                clientTransactionList.stream()
                .filter(t -> t.getDateCreated().isAfter(date))
                .collect(Collectors.toList());

    }

    @Override
    public List<Transaction> getAllTransactionsByClientIdBeforeDate(int clientId, LocalDate date) {
        var clientTransactionList= getAllTransactionsListByClientId(clientId);

        return
                clientTransactionList.stream()
                        .filter(t -> t.getDateCreated().isBefore(date))
                        .collect(Collectors.toList());

    }

    @Override
    public boolean deleteTransactionByUserIdAndTransactionId(int clientId, String transactionId){
        var userTransactionsOptional = clientTransactionRepository.findByUserClientId(clientId);

        if (userTransactionsOptional.isEmpty()){
            throw new ClientNotPresentException("Client with id " + clientId +  " does not have any transaction");
        }

        var map = userTransactionsOptional.get().getTransactions();
        var transactionList = map.get(getTransactionTypeByTransactionId(transactionId));

        var optionalTransactionToBeRemoved = userTransactionsOptional.get()
                .getTransactions()
                .get(getTransactionTypeByTransactionId(transactionId))
                .stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst();

        if (optionalTransactionToBeRemoved.isEmpty()){
            throw new TransactionNotPresentException();
        }

        transactionList.remove(optionalTransactionToBeRemoved.get());
        clientTransactionRepository.save(userTransactionsOptional.get());
        return true;
    }

    private Optional<AccountDTO> getAccountDetailsInAccountManagementService(int clientId, Transaction transactionRequested, int account) {
        Optional<AccountDTO> accountOptional;

        try{
            accountOptional
                    = Optional.ofNullable(accountManagementService.getAccountByIds(account));
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + ", accountNo: " + account + " does not exist.");
        }

        return accountOptional;
    }


    // ==================== Utility methods starts here ===============================================
    private Optional<Transaction> getTransactionByIdAndType(
            String transactionId, Map<TransactionType,
            List<Transaction>> map,
            TransactionType transactionType) {
        var deposits = map.get(transactionType);

       return deposits.stream()
                .filter(t -> t.getTransactionId().equals(transactionId)).findFirst();

    }



    private TransactionType getTransactionTypeByTransactionId(String transactionId){
        TransactionType transactionType;
        var transactionCode = transactionId.substring(0,3);

        switch (transactionCode){
            case "PAY":
                transactionType = TransactionType.PAYMENT;
                break;
            case "DEP":
                transactionType = TransactionType.DEPOSIT;
                break;
            case "ETR":
                transactionType = TransactionType.ETRANSFER;
                break;
            case "TRA":
                transactionType = TransactionType.TRANSFER;
                break;
            case "WIT":
                transactionType = TransactionType.WITHDRAWAL;
                break;
            default:
                throw new TransactionNotPresentException();
        }

        return transactionType;
    }

    // ==================== Utility methods ends here ===============================================

}