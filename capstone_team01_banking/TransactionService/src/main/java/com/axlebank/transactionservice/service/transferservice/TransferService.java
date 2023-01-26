package com.axlebank.transactionservice.service.transferservice;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.Role;
import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.model.TransactionType;
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
import java.util.List;
import java.util.Optional;

@Service
public class TransferService implements TransferServiceSpec {

    private final AccountManagementServiceClient accountManagementService;
    private final ClientService clientService;
    private final EmailServiceClient emailServiceClient;

    public TransferService(AccountManagementServiceClient accountManagementService, ClientService clientService, EmailServiceClient emailServiceClient) {
        this.accountManagementService = accountManagementService;
        this.clientService = clientService;
        this.emailServiceClient = emailServiceClient;
    }

    @Override
    public List<Transaction> makeTransfer(Transaction transactionRequested, int fromClientId) {
        List<Transaction> transactionsProcessedList = new ArrayList<>();
        var fromClientOptional
                = getVerifyClientByIdDetailsFromClientService(fromClientId, transactionRequested);

        if (fromClientOptional.isEmpty()){
            throw new ClientNotPresentException("Client with id: " + fromClientId + " does not exist");
        }

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);
        var fromAccountOptional = getAccountDetailsIfActiveInAccountManagementService(fromClientId, transactionRequested, transactionRequested.getFromAccountNumber());
        var toAccountOptional
                = getAccountDetailsIfActiveInAccountManagementService( fromClientId,transactionRequested, transactionRequested.getToAccountNumber());

        verifyFromAccountBelongsToFromClient(fromClientId, transactionRequested, fromAccountOptional);
        verifyFromAccountTypesIsCorrect(fromAccountOptional, transactionRequested);

        if (toAccountOptional.isEmpty()){
            throw new AccountNotPresentException(
                    "Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        if (fromAccountOptional.isEmpty()){
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        if (fromAccountOptional.get().getAccountNumber() == toAccountOptional.get().getAccountNumber()){
            throw new TransactionFailedException("FROM - TO Accounts cannot have same accountNo");
        }

        var fromAccountBalance =
                verifyAvailableFundsAndGetBalanceInFromAccount(transactionRequested, fromAccountOptional);

        int toClientId = toAccountOptional.get().getProfileId();
        var isTransferForSameClient = fromClientId == toClientId;

        if(isTransferForSameClient){
            // SAME CLIENT accounts
            verifyAccountDailyLimit(transactionRequested, fromAccountOptional);
            updateFromAccount(transactionRequested, amountRequested, fromAccountOptional, fromAccountBalance);
            updateToAccount(transactionRequested, amountRequested, toAccountOptional);

            var fromClientTransaction
                    = buildFromClientTransaction(transactionRequested, fromClientId, amountRequested);
            transactionsProcessedList.add(fromClientTransaction);

            return transactionsProcessedList;
        }else {
            // DIFFERENT CLIENT accounts
            /*
                WE KNOW IS DIFFERENT CLIENTS
                --------------------------------------------
                1. FROM ( (; -- $$$ ---> )
                    -> Verify fromClient
                        -> Exist?
                            - True --- CONTINUE
                            - false --- Throw ClientNotPresentException() ---- STOP PROCESS
                        -> IsProfileActive?
                             - True --- CONTINUE
                            -  false --- Throw ClientNotActiveException() ---- STOP PROCESS


                     -> Verify fromAccount ---- Get FromAccount by id ----- MS AccountManagementService
                                             1 check if account exist
                                                -> false --- throw AccountNotPresentException()
                                                -> true ---- CONTINUE

                                             2 Check account status
                                                -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                                                -> ACTIVE --- CONTINUE

                                             3 Funds verification
                                                3.4 accountBalance + overDraft >= $Requested
                                                    -> false ---- throw  FundsException();
                                                    -> true ---- CONTINUE

                                                3.5  Constraints verification
                                                    -> constraintExist?
                                                        -> true
                                                            -> constraintLimit <= $Requested
                                                                -> false ---- throw  DailyWithdrawalLimitException();
                                                                -> true ---- CONTINUE
                                                        -> false --- CONTINUE

                    --------------------------------------------
                 2. TO (  -- $$$ --->  (: )
                     -> Verify toClient
                        -> Exist?
                            - True --- CONTINUE
                            - false --- Throw ClientNotPresentException() ---- STOP PROCESS
                        -> IsProfileActive?
                             - True --- CONTINUE
                            -  false --- Throw ClientNotActiveException() ---- STOP PROCESS

                     -> Verify toAccount ---- Get FromAccount by id ----- MS AccountManagementService
                                             1 check if account exist
                                                -> false --- throw AccountNotPresentException()
                                                -> true ---- CONTINUE

                                             2 Check account status
                                                -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                                                -> ACTIVE --- CONTINUE
                      --------------------------------------------

                 3. -> Compute
                        -> deduct $$  fromAccount
                        -> add $$  toAccount

                 4. -> update accounts ---------- AccountManagement MS
                        -> FROM ----- call AccountManagementService --PUT (OptionalAccount)
                            -> true -- CONTINUE
                            -> false--
                        -> TO ----- call AccountManagementService --PUT (OptionalAccount)
                            -> true -- CONTINUE
                            -> false
                                -> return deducted funds to fromAccount --- call AccountManagementService --PUT (OptionalAccount)
                                -> throw Exception FailedToUpdatedAccount()

                 5. -> Build Transactions and add them to List to be saved in database
                                    Transactions
                                        1 fromAccount - REQUEST
                                              transactionId = Autogenerated
                                              amount = amountReceived ( - )  - Requested
                                              dateCreated = Autogenerated
                                              transactionType = TRANSFER
                                              clientId = clientId
                                              adminId = adminId - Requested
                                              transactionMedium = transactionMedium - Requested
                                              fromAccountNumber = fromAccountNumber - Requested
                                              toAccountNumber = toAccountNumber - Requested
                                              emailRecipient = ""
                                              institutionId = 0

                                        2 toAccount
                                              transactionId = Autogenerated
                                              amount = amountReceived ( + )  - Requested
                                              dateCreated = Autogenerated
                                              transactionType = TRANSFER
                                              clientId = toClientId -- get client using toAccount
                                              adminId = adminId - Requested
                                              transactionMedium = transactionMedium - Requested
                                              fromAccountNumber = fromAccountNumber - Requested
                                              toAccountNumber = toAccountNumber - Requested
                                              emailRecipient = ""
                                              institutionId = 0

                      -> SAVE Transactions to DB
             */

            Optional<ClientDTO> toClientOptional = getVerifyClientByIdDetailsFromClientService(toClientId, transactionRequested);
            updateFromAccount(transactionRequested, amountRequested, fromAccountOptional, fromAccountBalance);
            updateToAccount(transactionRequested, amountRequested, toAccountOptional);

            var fromClientTransaction
                    = buildFromClientTransaction(transactionRequested, fromClientId, amountRequested);

            var toClientTransaction
                    = buildToClientTransaction(transactionRequested, toClientId, amountRequested);

            transactionsProcessedList.add(fromClientTransaction);
            transactionsProcessedList.add(toClientTransaction);

            return transactionsProcessedList;
        }
    }

    private Transaction buildToClientTransaction(Transaction transactionRequested, int toClientId, double amountRequested) {
        var isCreatedByClient = transactionRequested.getAdminId() == 0;
        var createdBy = isCreatedByClient? Role.CLIENT: Role.ADMIN;

        var toTransaction = new Transaction(
                amountRequested * -1,
                transactionRequested.getTransactionType(),
                transactionRequested.getAdminId(),
                transactionRequested.getTransactionMedium(),
                transactionRequested.getFromAccountNumber(),
                transactionRequested.getToAccountNumber(),
                "",
                0,
                toClientId);// only thing that changes
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
                "",
                0,
                fromClientId);
        fromTransaction.setCreatedBy(createdBy);

        return fromTransaction;
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

    private Optional<AccountDTO> updateAccountDetailsInAccountService(AccountDTO accountDTO, int accountNumber, Transaction transactionRequested) {
        AccountDTO accountOptional;
        try{
            accountOptional =  accountManagementService.updateAccount(accountDTO, accountNumber);
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }
        return Optional.ofNullable(accountOptional);
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
}
