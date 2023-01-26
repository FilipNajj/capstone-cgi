package com.axlebank.transactionservice.service.etransferservice;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.Role;
import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.service.feignclients.AccountManagementServiceClient;
import com.axlebank.transactionservice.service.feignclients.ClientService;
import com.axlebank.transactionservice.service.feignclients.EmailServiceClient;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.*;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotActiveException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.AmountRequestedException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.FundsException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.TransactionFailedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class EtransferService implements EtransferServiceSpec{

    private final AccountManagementServiceClient accountManagementService;
    private final ClientService clientService;
    private final EmailServiceClient emailServiceClient;

    public EtransferService(AccountManagementServiceClient accountManagementService, ClientService clientService, EmailServiceClient emailServiceClient) {
        this.accountManagementService = accountManagementService;
        this.clientService = clientService;
        this.emailServiceClient = emailServiceClient;
    }

    @Override
    public List<Transaction> makeETransfer(Transaction transactionRequested, int fromClientId) {

        var fromClientOptional
                = getVerifyClientByIdDetailsFromClientService(fromClientId, transactionRequested);

        if (fromClientOptional.isEmpty()){
            throw new ClientNotPresentException("Client with id: " + fromClientId + " does not exist");
        }

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);
        var fromAccountOptional = getAccountDetailsIfActiveInAccountManagementService(fromClientId, transactionRequested, transactionRequested.getFromAccountNumber());
        var fromAccountBalance =
                verifyAvailableFundsAndGetBalanceInFromAccount(transactionRequested, fromAccountOptional);

        verifyFromAccountBelongsToFromClient(fromClientId, transactionRequested, fromAccountOptional);
        verifyFromAccountTypesIsCorrect(fromAccountOptional, transactionRequested);

        if (fromAccountOptional.isEmpty()){
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        var optionalReceiver = getVerifyClientByEmailDetailsFromClientService(transactionRequested.getEmailRecipient());
        if(optionalReceiver.isEmpty()){
            throw new ClientNotPresentException("Client with the e-mail: " + transactionRequested.getEmailRecipient() + " does not exist");
        }
        ClientDTO receiver = optionalReceiver.get();

        var toAccountOptionalList
                = getAccountsByClientProfileIdClientService(receiver.getProfileId());

        if(toAccountOptionalList.isEmpty()){
            throw new AccountNotPresentException("No account from this client exist");
        }

        List<AccountDTO> toAccountList = toAccountOptionalList.get();
        AccountDTO toAccount = null;
        int i = 0;
        int toAccNUm = 0;
        do {
            Optional<AccountDTO> optionalToAccount = getAccountDetailsIfActiveInAccountManagementService(
               toAccountList.get(i).getProfileId(), transactionRequested, toAccountList.get(i).getAccountNumber()
            );
            if(optionalToAccount.isPresent()){
                toAccount = optionalToAccount.get();
            }
            i++;
        }while(null == toAccount && i < toAccountList.size()+1);
        if(i > toAccountList.size()){
            throw new AccountNotActiveException("No account are active for this receiver.");
        }

        if (fromAccountOptional.get().getAccountNumber() == toAccount.getAccountNumber()){
            throw new TransactionFailedException("FROM - TO Accounts cannot have same accountNo");
        }
        updateFromAccount(transactionRequested, amountRequested, fromAccountOptional, fromAccountBalance);
        updateToAccount(transactionRequested, amountRequested, Optional.of(toAccount));

        var fromClientTransaction
                = buildFromClientTransaction(transactionRequested, fromClientId, amountRequested);

        var toClientTransaction
                = buildToClientTransaction(transactionRequested, receiver.getProfileId(), amountRequested, toAccount.getAccountNumber());
        List<Transaction> transactionsProcessedList = new ArrayList<>();
        transactionsProcessedList.add(fromClientTransaction);
        transactionsProcessedList.add(toClientTransaction);

        ClientDTO fromClient = fromClientOptional.get();
        EtranferDTO etranferDTO = new EtranferDTO(fromClient.getFirstName(), fromClient.getEmail(),
                receiver.getFirstName(), receiver.getEmail(), "EMT", abs(transactionRequested.getAmount()));
        try{
            emailServiceClient.sendEtransfer(etranferDTO);
        }catch(Exception e){
            System.out.println("Transaction processed but the confirmation e-mails could not be sent.");
            // TODO LOGGER in case Email Server is down
            return transactionsProcessedList;
        }

        return transactionsProcessedList;
    }

    private Transaction buildToClientTransaction(Transaction transactionRequested, int toClientId, double amountRequested,
                                                 int toAccount) {
        var isCreatedByClient = transactionRequested.getAdminId() == 0;
        var createdBy = isCreatedByClient? Role.CLIENT: Role.ADMIN;

        var toTransaction = new Transaction(
                amountRequested * -1,
                transactionRequested.getTransactionType(),
                transactionRequested.getAdminId(),
                transactionRequested.getTransactionMedium(),
                transactionRequested.getFromAccountNumber(),
                toAccount,
                "",
                0,
                toClientId);
        toTransaction.setCreatedBy(createdBy);
        return toTransaction;
    }

    private Transaction buildFromClientTransaction(Transaction transactionRequested, int fromClientId, double amountRequested) {
        var isCreatedByClient = transactionRequested.getAdminId() == 0;
        var createdBy = isCreatedByClient? Role.CLIENT: Role.ADMIN;

        var fromTransaction = new Transaction(
                amountRequested,
                transactionRequested.getTransactionType(),
                transactionRequested.getAdminId(),
                transactionRequested.getTransactionMedium(),
                transactionRequested.getFromAccountNumber(),
                transactionRequested.getToAccountNumber(),
                transactionRequested.getEmailRecipient(),
                0,
                fromClientId);
        fromTransaction.setCreatedBy(createdBy);

        return fromTransaction;
    }

    private Optional<ClientDTO> getVerifyClientByIdDetailsFromClientService(int clientId, Transaction transactionRequested) {
        Optional<ClientDTO> clientOptional;
        try{
            clientOptional = Optional.ofNullable(clientService.getClientById(clientId));
        }catch(Exception e){
            throw new ClientNotPresentException("Client with this ID doesnt exists.");
        }

        if (clientOptional.isEmpty()){
            throw new ClientNotPresentException("Client with id: " + clientId + " does not exist");
        }

        if (!clientOptional.get().getProfileStatus().equals(ProfileStatus.ACTIVE)){
            throw new ClientNotActiveException(
                    "Cannot process " +
                            transactionRequested.getTransactionType() + " " +
                            ProfileStatus.class.getSimpleName() + " [" + clientOptional.get().getProfileStatus() + "]");

        }

        return clientOptional;
    }

    private Optional<ClientDTO> getVerifyClientByEmailDetailsFromClientService(String email) {
        Optional<ClientDTO> clientOptional;
        try{
            clientOptional = Optional.ofNullable(clientService.getClientByEmail(email));
        }catch(Exception e){
            throw new ClientNotPresentException("Client with this ID doesnt exists.");
        }

        if (clientOptional.isEmpty()){
            throw new ClientNotPresentException("No client with the e-mail: " + email + " exists.");
        }

        return clientOptional;
    }

    private Optional<List<AccountDTO>> getAccountsByClientProfileIdClientService(int profileId) {
        Optional<List<AccountDTO>> accountOptional = Optional.ofNullable(accountManagementService.getAccountByProfileId(profileId));

        if (accountOptional.isEmpty()){
            throw new AccountNotPresentException("No account from this client exist");
        }

        return accountOptional;
    }

    private double verifyIsDeductionTransactionAndGetAmount(Transaction transactionRequested) {
        var amountRequested = transactionRequested.getAmount();

        if (amountRequested > 0 || amountRequested == 0){
            throw new AmountRequestedException(
                    transactionRequested.getTransactionType() +
                            " is a minus operation please ensure amount: is less than zero eg. -150");

        }
        return amountRequested;
    }

    private Optional<AccountDTO> getAccountDetailsIfActiveInAccountManagementService(int clientId, Transaction transactionRequested, int account) {
        Optional<AccountDTO> accountOptional;

        try{
            accountOptional
                    = Optional.ofNullable(accountManagementService.getAccountByIds(account));
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + account + " does not exist.");
        }

        return getAccountOptionalIfAccountIsActive(transactionRequested, accountOptional);
    }

    private Optional<AccountDTO> getAccountOptionalIfAccountIsActive(Transaction transactionRequested, Optional<AccountDTO> accountOptional) {
        var transactionType = transactionRequested.getTransactionType();

        if (accountOptional.isPresent()){
            var accountStatus = accountOptional.get().getAccountStatus();
            if (!accountStatus.equals(AccountStatus.ACTIVE)){
                throw new AccountNotActiveException( "Cannot process " +
                        transactionType +
                        " in accountNo " + accountOptional.get().getAccountNumber() + " , Reason: " +
                        AccountStatus.class.getSimpleName() + " [" + accountStatus + "]");
            }

        }

        return accountOptional;
    }

    private void verifyFromAccountBelongsToFromClient(int fromClientId, Transaction transactionRequested,Optional<AccountDTO> fromAccountOptional){
        if (fromAccountOptional.isEmpty()){
            throw new AccountNotPresentException("AccountNo " + transactionRequested.getFromAccountNumber() + " not present.");
        }
        if (fromClientId != fromAccountOptional.get().getProfileId()){
            throw new AccountNotBelongsToClientException(
                    "Account: " + transactionRequested.getFromAccountNumber() + " does not belong to profileId: " + fromClientId);
        }
    }

    private void verifyFromAccountTypesIsCorrect(Optional<AccountDTO> fromAccountOptional, Transaction transactionRequested){
        if (fromAccountOptional.isEmpty()){
            throw new AccountNotPresentException("AccountNo " + transactionRequested.getFromAccountNumber() + " not present.");
        }

        if (!(fromAccountOptional.get().getAccountType().toString()
                .equals(transactionRequested.getTransactionMedium().toString()))){
            throw new AccountTypeException("Account type mismatched: Requested AccountType = " + transactionRequested.getTransactionMedium() + ",  in file AccountType = " +
                    fromAccountOptional.get().getAccountType());
        }

    }

    private double verifyAvailableFundsAndGetBalanceInFromAccount(Transaction transactionRequested, Optional<AccountDTO> fromAccountDTO) {
        if (fromAccountDTO.isEmpty()){
            throw new AccountNotPresentException("AccountNo: " + transactionRequested.getFromAccountNumber() + "  does not exist");
        }
        var accountBalance = fromAccountDTO.get().getBalance();
        var overDraft = fromAccountDTO.get().getOverDraft();
        verifyAccountDailyLimit(transactionRequested, fromAccountDTO);

        var amountRequested = transactionRequested.getAmount();

        var totalAvailableFunds = accountBalance + overDraft;
        if (!(totalAvailableFunds + amountRequested >= 0)){
            throw new FundsException("Insufficient funds in account");
        }
        return accountBalance;
    }

    private void verifyAccountDailyLimit(Transaction transactionRequested,
                                         Optional<AccountDTO> fromAccountDTO) {
        var transactionType = transactionRequested.getTransactionType();
        double dailyLimit;

        if (fromAccountDTO.isEmpty()){
            throw new AccountNotPresentException("AccountNo: "  + transactionRequested.getFromAccountNumber() + " does not exist in file");
        }
        switch (transactionType){
            case WITHDRAWAL:{
                dailyLimit = fromAccountDTO.get().getMaxDailyWithdrawlLimit();
                if ( !(dailyLimit + transactionRequested.getAmount() >= 0) ){
                    throw new DailyWithdrawalLimitException(
                            "Cannot process " + transactionRequested.getTransactionType() +
                                    ", maximum daily withdrawal Limit: " + fromAccountDTO.get().getMaxDailyWithdrawlLimit());
                }
                break;
            }
            case PAYMENT:{
                dailyLimit = fromAccountDTO.get().getMaxDailyPurchasingLimit();
                if ( !(dailyLimit + transactionRequested.getAmount() >= 0) ){
                    throw new DailyPurchaseLimitException(
                            "Cannot process " + transactionRequested.getTransactionType() +
                                    ", maximum daily purchase Limit: " + fromAccountDTO.get().getMaxDailyWithdrawlLimit());
                }
                break;
            }
        }
    }

    private void updateToAccount(Transaction transactionRequested, double amountRequested, Optional<AccountDTO> toAccountOptional){
        if (toAccountOptional.isEmpty()){
            throw new AccountNotPresentException("Account not present");
        }
        var toAccountNewBalance = toAccountOptional.get().getBalance() + (amountRequested * -1);
        toAccountOptional.get().setBalance(toAccountNewBalance);

        var toAccountOptionalUpdated = updateAccountDetailsInAccountService(
                toAccountOptional.get(),
                toAccountOptional.get().getAccountNumber(),
                transactionRequested);

        if (toAccountOptionalUpdated.isEmpty()){
            throw new AccountNotPresentException(
                    "Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        var isToAccountUpdated = (toAccountOptionalUpdated.get().getBalance() == toAccountNewBalance );

        if (!isToAccountUpdated){
            throw new TransactionFailedException("Transaction failed: Error updating accountNo: " + toAccountOptional.get().getAccountNumber()  + " in Account Service");
        }
    }

    private void updateFromAccount(Transaction transactionRequested, double amountRequested, Optional<AccountDTO> fromAccountOptional, double fromAccountBalance) {
        if (fromAccountOptional.isPresent()){
            fromAccountOptional.get().setBalance(fromAccountBalance - (amountRequested * -1));

            var fromAccountOptionalUpdated = updateAccountDetailsInAccountService(
                    fromAccountOptional.get(),
                    fromAccountOptional.get().getAccountNumber(),
                    transactionRequested);

            if (fromAccountOptionalUpdated.isEmpty()){
                throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
            }

            boolean isFromAccountUpdated =
                    (fromAccountOptionalUpdated.get().getBalance() - fromAccountBalance == amountRequested);

            if (!isFromAccountUpdated){
                throw new TransactionFailedException("Failed to updated account in Account Service");
            }
        }
    }
    private Optional<AccountDTO> updateAccountDetailsInAccountService(AccountDTO accountDTO, int accountNumber, Transaction transactionRequested) {
        AccountDTO accountOptional;
        try{
            accountOptional =  accountManagementService.updateAccount(accountDTO, accountNumber);
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }
        return Optional.ofNullable(accountOptional);
    }

}
