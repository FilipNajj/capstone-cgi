package com.axlebank.transactionservice.service;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.*;
import com.axlebank.transactionservice.repository.ClientTransactionRepository;
import com.axlebank.transactionservice.service.feignclients.AccountManagementServiceClient;
import com.axlebank.transactionservice.service.feignclients.ClientService;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.*;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotActiveException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.institutionexceptions.InstitutionNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionServiceSpec{

    private final ClientTransactionRepository clientTransactionRepository;
    private final ClientService clientService;
    private final AccountManagementServiceClient accountManagementService;

    public TransactionService(ClientTransactionRepository clientTransactionRepository, ClientService clientService, AccountManagementServiceClient accountManagementService) {
        this.clientTransactionRepository = clientTransactionRepository;
        this.clientService = clientService;
        this.accountManagementService = accountManagementService;
    }


    // ======================================== Add new Transaction starts here =================================================================
    @Override
    public Transaction newTransaction(Transaction transactionRequested, int clientId) {
            List<Transaction> transactionsProcessedList = new ArrayList<>();

            switch (transactionRequested.getTransactionType()){
                case DEPOSIT: {
                    if (!makeDeposit(transactionRequested, clientId, transactionsProcessedList))
                        throw new TransactionFailedException("Failed to DEPOSIT, account-service failed to update - server under under testing,  please try later");
                }
                break;

                case WITHDRAWAL: {
                    if (!makeWithdrawal(transactionRequested, clientId, transactionsProcessedList))
                        throw new TransactionFailedException(
                                "Failed to WITHDRAWAL, account-service failed to update - server under under testing,  please try later");
                }
                break;

                case PAYMENT:{
                    if (!makePayment(transactionRequested, clientId, transactionsProcessedList)){
                        throw new TransactionFailedException(
                                "Failed to PAYMENT, account-service failed to update - server under under testing,  please try later");
                    }
                }
                break;

                case TRANSFER: if (!makeTransfer(transactionRequested, clientId, transactionsProcessedList)){
                    throw new TransactionFailedException(
                            "Failed to TRANSFER, account-service failed to update - server under under testing,  please try later");
                }
                break;

                case ETRANSFER:makeETransfer(transactionRequested, clientId);
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

    private boolean makeDeposit(Transaction transactionRequested, int clientId, List<Transaction> transactionsProcessedList) throws FundsException {
        //TODO
        /*

            DEPOSIT

            1 -> Verify amountReceived > 0
                -> true ---- CONTINUE
                -> false ---- throw FundsException("Funds amount must be greater than 0.")

            2 -> Get ToAccount by id  ( ---- $$$ -> To Client account  ) ----- MS AccountManagementService
                 2.1 check if account exist
                    -> false --- throw AccountNotPresentException()
                    -> true ---- CONTINUE

                 2.2 Check account status
                    -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                    -> ACTIVE --- CONTINUE

            3 -> Get Institution
                -> verify it exist
                    - false -------------   throw InstitutionNotPresentException()  --- STOP PROCESS
                    - true ---------- CONTINUE

            4 -> Compute (+)
               4.1 Add $$$ (balance + amountReceived)

            5. Transaction Status
                5.1 Successful
                    5.1.1 Make request to Account-MS to update account --- PUT Request
                        -> True
                            -> Save Transaction
                            -> return transaction to FE
                        -> false
                            -> Go to 5.2

                    5.2 Failed
                        5.2.1 -> throw TransactionFailedException()  --- STOP PROCESS


         */

        var amountReceived =  transactionRequested.getAmount();
        if (amountReceived < 0){
            throw new FundsException("Funds amount must be greater than $0.00");
        }

        getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);
        Optional<AccountDTO> toAccountDTO =
                getToAccountDetailsFromAccountManagementService(transactionRequested);

        if (transactionRequested.getTransactionMedium().equals(TransactionMedium.PAYROLL)){
            verifyIfInstitutionExistAtInstitutionService(transactionRequested.getInstitutionId(), transactionRequested);
        }

        boolean isDeposited = false;

        if (toAccountDTO.isPresent()){
            var toAccountBalance = toAccountDTO.get().getBalance();
            toAccountDTO.get().setBalance(toAccountBalance + amountReceived);

            // TODO -> use line 197 to verify if account was updated
            var toAccountDTOUpdated = updateAccountDetailsInAccountService(toAccountDTO.get(), toAccountDTO.get().getAccountNumber(), transactionRequested);

            // Optional<AccountDTO> toAccountDTOUpdated =  getToAccountDetailsFromAccountManagementService(clientId, transactionRequested);
            if (toAccountDTOUpdated.isEmpty()){
                throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
            }

            isDeposited =  (toAccountDTOUpdated.get().getBalance() == toAccountDTO.get().getBalance() &&
                    toAccountDTOUpdated.get().getAccountNumber() == toAccountDTO.get().getAccountNumber());

        }

        if (isDeposited){

           var transactionMedium = transactionRequested.getTransactionMedium();

           switch (transactionMedium){
               case CASH:

               case CHEQUE: {

                   var transaction = new Transaction(amountReceived, transactionRequested.getTransactionType(),
                                transactionRequested.getAdminId(), transactionMedium, 0, transactionRequested.getToAccountNumber(),
                                "", 0, clientId
                           );
                   transaction.setCreatedBy(Role.ADMIN);
                   transactionsProcessedList.add(transaction);
               }
               break;

               case PAYROLL:{
                   var transaction
                           = new Transaction(amountReceived, transactionRequested.getTransactionType(),
                           transactionRequested.getAdminId(), transactionMedium, 0, transactionRequested.getToAccountNumber(),
                           "", transactionRequested.getInstitutionId(), clientId
                   );
                   transaction.setCreatedBy(Role.ADMIN);
                   transactionsProcessedList.add(transaction);
               }
               break;

           }
        }

        return isDeposited;

    }

    private boolean makeWithdrawal(Transaction transactionRequested, int clientId, List<Transaction> transactionsProcessedList) {
        /*
            WITHDRAWAL
           1 -> Verify amountReceived < 0
                -> true ---- CONTINUE
                -> false ---- throw AmountRequestedException();

           2 -> Verify Client  --- Get client by id ( <---- $$$ --- From Client account ) ---- MS ClientService
                    2.1 check if client exist
                        -> false  ---- throw ClientNotPresentException()
                        -> true --- CONTINUE

                    2.2 check profile status
                        2.2.2
                                ->  NOT ACTIVE  -- throw ClientInactiveException()  --- STOP PROCESS
                                -> ACTIVE --- CONTINUE

            3 -> Verify Account ---- Get FromAccount by id  ( <---- $$$ -- Client account  ) ----- MS AccountManagementService
                 3.1 check if account exist
                    -> false --- throw AccountNotPresentException()
                    -> true ---- CONTINUE

                 3.2 Check account status
                    -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                    -> ACTIVE --- CONTINUE

                 3.3 Funds  verification
                    3.4 accountBalance + overDraft >= $Requested
                        -> false ---- throw  FundsException();
                        -> true ---- CONTINUE

                    3.5  Constraints verification
                        -> dailyWithdrawlLimit <= $Requested
                            -> false ---- throw  DailyWithdrawalLimitException();
                            -> true ---- CONTINUE

            4 -> Compute (+)
               4.1 Minus $$$ (accountBalance - amountRequested)

            5. Transaction Status
                5.1 Successful
                    5.1.1 Make request to Account-MS to update account --- PUT Request
                        -> True
                            -> Save Transaction
                            -> return transaction to FE
                        -> false
                            -> Go to 5.2

                    5.2 Failed
                        5.2.1 -> throw TransactionFailedException()  --- STOP PROCESS



         */

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);


        getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);

        var fromAccountDTO = getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

        if (fromAccountDTO.isEmpty()){
            throw new AccountNotPresentException(
                    "Cannot process " + transactionRequested.getTransactionType() +
                            ", accountNo: " + transactionRequested.getFromAccountNumber() +
                            " does not exist.");
        }

        double availableFundsInAccount
                = verifyAvailableFundsAndGetBalanceInAccount(transactionRequested,fromAccountDTO);

        fromAccountDTO.get().setBalance(availableFundsInAccount + amountRequested);

        updateAccountDetailsInAccountService(
                fromAccountDTO.get(),
                fromAccountDTO.get().getAccountNumber(),
                transactionRequested);

        boolean isAccountUpdated = false;

        var fromAccountDTOUpdatedOptional =
                getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

        if(fromAccountDTOUpdatedOptional.isPresent()){
            if (fromAccountDTOUpdatedOptional.get().getBalance() == availableFundsInAccount){
                isAccountUpdated = true;
            }
        }

        if (isAccountUpdated){
            var transactionMedium = transactionRequested.getTransactionMedium();
            switch (transactionMedium){
                case CHECKING:
                case CREDIT_CARD:
                case SAVING: {
                    var transaction= new Transaction(
                            amountRequested, transactionRequested.getTransactionType(),
                            transactionRequested.getAdminId(),
                            transactionMedium, transactionRequested.getFromAccountNumber(),
                            0, "", 0, clientId);

                    if ((transactionRequested.getAdminId() != 0)) {
                        transaction.setCreatedBy(Role.ADMIN);
                    } else {
                        transaction.setCreatedBy(Role.CLIENT);
                    }
                   transactionsProcessedList.add(transaction);
                }
                break;

                default:
                    throw new WithdrawalTransactionMediumException("Failed: Cannot make withdrawal from: " + transactionMedium);

            }
        }
        return isAccountUpdated;
    }


    private boolean makePayment(Transaction transactionRequested, int clientId, List<Transaction> transactionsProcessedList) {
        // TODO
        /*

         PAYMENT
            from ---------  $$$$ -----------> to
                    -> clientA [account-n] --- $$$ --> institution-n  (Bills, Daily Purchases)


                   ===================================== PAYMENT =============================================
            1 -> Get InstitutionDetails by id ( ---- $$$ ->  institution) ----- MS InstitutionService
                1.1 Verify Institution exist
                    -> false --- throw InstitutionNotPresentException()
                    -> true --- CONTINUE

            2 -> Verify amountReceived < 0
                -> true ---- CONTINUE
                -> false ---- throw AmountRequestedException();



            3 -> Verify Client  --- Get client by id ( <---- $$$ --- From Client account ) ---- MS ClientService
                    2.1 check if client exist
                        -> false  ---- throw ClientNotPresentException()
                        -> true --- CONTINUE

                    3.2 check profile status
                        3.2.2
                                -> NOT ACTIVE  -- throw ClientInactiveException()  --- STOP PROCESS
                                -> ACTIVE --- CONTINUE

            4 -> Get FromAccount by id  (From Client account ---- $$$ -> Inst ) ----- MS AccountManagementService
                4.1 check if account exist
                    -> false --- throw AccountNotPresentException()
                    -> true ---- CONTINUE

                4.2 Check fromAccount belongs to the client  --- using clientId
                    -> false --- throw AccountNotBelongsToClientException()
                    -> true --- CONTINUE

                4.3 Check account status
                    -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                    -> ACTIVE --- CONTINUE

                4.4 Funds  verification
                    4.4.1 accountBalance + overDraft >= $Requested
                            -> false ---- throw  FundsException();
                            -> true ---- CONTINUE

                    4.4.2  Constraints verification
                            -> maxDailyPurchasingLimit <= $Requested
                                -> false ---- throw  DailyWithdrawalLimitException();
                                -> true ---- CONTINUE
             5 -> Compute (+)
                 5.1 Minus $$$ (accountBalance - amountRequested)

             6. Transaction Status
                6.1 Successful
                    6.1.1 Make request to Account-MS to update account --- PUT Request
                        -> True
                            -> Save Transaction
                            -> return transaction to FE
                        -> false
                            -> Go to 5.2

                 6.2 Failed
                        6.2.1 -> throw TransactionFailedException()  --- STOP PROCESS

         */
        verifyIfInstitutionExistAtInstitutionService(transactionRequested.getInstitutionId(), transactionRequested);

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);


        getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);
        var fromAccountDTO = getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

        if (fromAccountDTO.isEmpty()){
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        double availableFundsInAccount
                = verifyAvailableFundsAndGetBalanceInAccount(transactionRequested,fromAccountDTO);

        fromAccountDTO.get().setBalance(availableFundsInAccount + amountRequested);

        updateAccountDetailsInAccountService(fromAccountDTO.get(), fromAccountDTO.get().getAccountNumber(), transactionRequested);

        boolean isAccountUpdated = false;

        var fromAccountDTOUpdatedOptional =
                getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

        if(fromAccountDTOUpdatedOptional.isPresent()){
            if (fromAccountDTOUpdatedOptional.get().getBalance() == availableFundsInAccount){
                isAccountUpdated = true;
            }
        }

        if (isAccountUpdated){
            var transactionMedium = transactionRequested.getTransactionMedium();
            switch (transactionMedium){
                case CHECKING:
                case CREDIT_CARD:
                case SAVING: {
                    var transaction= new Transaction(
                            amountRequested,
                            transactionRequested.getTransactionType(),
                            transactionRequested.getAdminId(),
                            transactionMedium,
                            transactionRequested.getFromAccountNumber(),
                            0, "", transactionRequested.getInstitutionId(),
                            clientId);

                    if ((transactionRequested.getAdminId() != 0)) {
                        transaction.setCreatedBy(Role.ADMIN);
                    } else {
                        transaction.setCreatedBy(Role.CLIENT);
                    }
                    transactionsProcessedList.add(transaction);
                }
                break;

                default:
                    throw new WithdrawalTransactionMediumException(
                            "Failed: Cannot make " + transactionRequested.getTransactionType() + " from: " + transactionMedium);

            }
        }
        return isAccountUpdated;
    }


    private boolean makeTransfer(Transaction transactionRequested, int clientId, List<Transaction> transactionsProcessedList) {
        /*
             TRANSFER
               A (-) --------- $$$  ------> B (+)

                SAME CLIENT
                        AccNo1 ----> AccNo2
                             ==> 2 transactions to be saved

                CLIENT_A ----> CLIENT_B
                    CLIENT_A AccNoN ---- $$$ ---> CLIENT_B AccNoN
                        ==> 2 transactions to be saved

               ---------------------------------------------------------
               1 -> Verify amountReceived < 0  ------ DONE
                    -> true ---- CONTINUE
                    -> false ---- throw AmountRequestedException();


               2 -> Verify if transfer is for SAME Client
                        -> true ---  AccNo1 ----> AccNo2
                            -> verify client
                                1 check if client exist
                                    -> false  ---- throw ClientNotPresentException()
                                    -> true --- CONTINUE

                                2 check profile status
                                    2.2
                                        -> NOT ACTIVE  -- throw ClientInactiveException()  --- STOP PROCESS
                                        -> ACTIVE --- CONTINUE

                            -> verify accounts
                                    FROM_ACCOUNT
                                         3 -> Verify Account ---- Get FromAccount by id ----- MS AccountManagementService
                                            3.1 check if account exist
                                                -> false --- throw AccountNotPresentException()
                                                -> true ---- CONTINUE

                                             3.2 Check account status
                                                -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                                                -> ACTIVE --- CONTINUE

                                             3.3 Funds verification
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
                                    TO_ACCOUNT
                                        3 -> Verify Account ---- Get FromAccount by id ----- MS AccountManagementService
                                            3.1 check if account exist
                                                -> false --- throw AccountNotPresentException()
                                                -> true ---- CONTINUE

                                             3.2 Check account status
                                                -> PENDING, CLOSED, SUSPENDED, FAULT, LOCKED --- throw AccountNotActiveException()  --- STOP PROCESS
                                                -> ACTIVE --- CONTINUE
                              -> Compute
                                    -> deduct $$ fromAccount
                                    -> add $$ toAccount

                              -> update accounts ---------- AccountManagement MS

                              -> verify both update were successful
                                -> false  --- Throw Exception
                                -> true --- Continue

                              -> Build Transactions and add them to List to be saved in database
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
                                              clientId = clientId
                                              adminId = adminId - Requested
                                              transactionMedium = transactionMedium - Requested
                                              fromAccountNumber = fromAccountNumber - Requested
                                              toAccountNumber = toAccountNumber - Requested
                                              emailRecipient = ""
                                              institutionId = 0

                                -> SAVE Transactions to DB

                        -> false --- CLIENT_A AccNoN ---- $$$ ---> CLIENT_B AccNoN


         */

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);
        var toAccountOptional = getToAccountDetailsFromAccountManagementService(transactionRequested);

        if (toAccountOptional.isEmpty()){
            throw new AccountNotPresentException(
                    "Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        int toClientId = toAccountOptional.get().getProfileId();
        var isTransferForSameClient = clientId == toClientId;

       if (isTransferForSameClient){
           // SAME CLIENT accounts
           var optionalClient = getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);
           if (optionalClient.isEmpty()){
               throw new ClientNotPresentException("Client with id: " + clientId + " does not exist");
           }

           var fromAccountOptional = getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);
           if (fromAccountOptional.isEmpty()){
               throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
           }

           var fromAccountBalance =
                   verifyAvailableFundsAndGetBalanceInAccount(transactionRequested, fromAccountOptional);
           verifyAccountDailyLimit(transactionRequested, fromAccountOptional);
           var toAccountNewBalance = toAccountOptional.get().getBalance() + (amountRequested * -1);
           fromAccountOptional.get().setBalance(fromAccountBalance + amountRequested);
           toAccountOptional.get().setBalance(toAccountNewBalance);

           updateAccountDetailsInAccountService(
                   fromAccountOptional.get(),
                   fromAccountOptional.get().getAccountNumber(),
                   transactionRequested);

           var fromAccountOptionalUpdated
                   = getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

           if (fromAccountOptionalUpdated.isEmpty()){
               throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
           }
           boolean isFromAccountUpdated =
                   (fromAccountOptionalUpdated.get().getBalance() - fromAccountBalance  == amountRequested );

           if (!isFromAccountUpdated){
               throw new TransactionFailedException("Failed to updated account in Account Service");
           }

           updateAccountDetailsInAccountService(
                   toAccountOptional.get(),
                   toAccountOptional.get().getAccountNumber(),
                   transactionRequested);

           var toAccountOptionalUpdated
                   = getFromAccountDetailsFromAccountManagementService(clientId, transactionRequested);

           if (toAccountOptionalUpdated.isEmpty()){
               throw new AccountNotPresentException(
                       "Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
           }

           var isToAccountUpdated = (toAccountOptionalUpdated.get().getBalance() == toAccountNewBalance );

           if (!isToAccountUpdated){
               throw new TransactionFailedException("Transaction failed: Error updating accountNo: " + toAccountOptional.get().getAccountNumber()  + " in Account Service");
           }

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
                   transactionRequested.getInstitutionId(), clientId);
           fromTransaction.setCreatedBy(createdBy);

           var toTransaction = new Transaction(
                   amountRequested * -1,
                   transactionRequested.getTransactionType(),
                   transactionRequested.getAdminId(),
                   transactionRequested.getTransactionMedium(),
                   transactionRequested.getFromAccountNumber(),
                   transactionRequested.getToAccountNumber(),
                   "",
                   transactionRequested.getInstitutionId(), clientId);
           toTransaction.setCreatedBy(createdBy);


           transactionsProcessedList.add(fromTransaction);
           transactionsProcessedList.add(toTransaction);

           return true;

       }else {
           // TODO -- Client A accountN ---> Client B AccountN
            return false;
       }

    }



    private Transaction makeETransfer(Transaction transactionRequested, int clientId) {
        // TODO
        /*

         */
        return null;
    }

    // -----------------------------------------------------------------------------------------

    private void verifyAccountDailyLimit(Transaction transactionRequested,
                                         Optional<AccountDTO> fromAccountDTO) {
        var transactionType = transactionRequested.getTransactionType();
        double dailyLimit;

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

    private double verifyIsDeductionTransactionAndGetAmount(Transaction transactionRequested) {
        var amountRequested = transactionRequested.getAmount();

        if (amountRequested > 0 || amountRequested == 0){
            throw new AmountRequestedException(
                    transactionRequested.getTransactionType() +
                            " is a minus operation please ensure amount: is less than zero eg. -150");

        }

        return amountRequested;
    }

    private double verifyAvailableFundsAndGetBalanceInAccount(Transaction transactionRequested, Optional<AccountDTO> fromAccountDTO) {
        var accountBalance = fromAccountDTO.get().getBalance();
        var overDraft = fromAccountDTO.get().getOverDraft();
        var amountRequested = transactionRequested.getAmount();
        verifyAccountDailyLimit(transactionRequested, fromAccountDTO);

        var totalAvailableFunds = accountBalance + overDraft;
        if (!(totalAvailableFunds + amountRequested >= 0)){
            throw new FundsException("Insufficient funds in account");
        }
        return accountBalance;
    }

    private boolean verifyIfInstitutionExistAtInstitutionService(int institutionId, Transaction transactionRequested) {
        // TODO-> Feign Client Call
        boolean isInstitutionPresent = true;
        if (institutionId == 0)
            isInstitutionPresent = false;
        //  TODO ------ delete above

        if (!isInstitutionPresent){
            throw new InstitutionNotPresentException("Cannot process " +
                    transactionRequested.getTransactionType() +
                    " , Reason: institutionNo " +
                    institutionId +  " does not exist, please contact one of our admin representative");
        }
        return isInstitutionPresent;
    }


    private Optional<AccountDTO> updateAccountDetailsInAccountService(AccountDTO accountDTO, int accountNumber, Transaction transactionRequested) {
// TODO -> Feign Client Call
        //        Optional<AccountDTO> optionalUpdatedAccountDTO;
//        try{
//           optionalUpdatedAccountDTO =
//                   Optional.ofNullable(accountManagementService.updateAccount(accountDTO, accountNumber));
//        } catch (Exception e) {
//            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
//        }

        Optional<AccountDTO> optionalUpdatedAccountDTO
                = Optional.of(new AccountDTO(
                1255, 2, 1000.00,
                1000.00, 500, LocalDate.now(), 555,
                AccountStatus.ACTIVE, AccountType.CHECKING));

        return optionalUpdatedAccountDTO;
    }

    private Optional<AccountDTO> getToAccountDetailsFromAccountManagementService(Transaction transactionRequested) {
        // TODO -> Feign Client Call
//        try{
//            accountOptional = Optional.ofNullable(accountManagementService.getAccountByIds(transactionRequested.getFromAccountNumber()));
//        } catch (Exception e) {
//            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
//        }
        Optional<AccountDTO> accountOptional
                = Optional.of(new AccountDTO(
                1255, 0.00, 1000.00,
                1000.00, 500, LocalDate.now(), 555,
                AccountStatus.ACTIVE, AccountType.CHECKING));

        if (accountOptional.isEmpty()){
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
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

    private Optional<AccountDTO> getFromAccountDetailsFromAccountManagementService(int clientId, Transaction transactionRequested) {
        // TODO -> Feign Client Call
//        Optional<AccountDTO> accountOptional = Optional.empty();
//
//        try{
//            accountOptional = Optional.ofNullable(accountManagementService.getAccountByIds(transactionRequested.getFromAccountNumber()));
//        } catch (Exception e) {
//            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
//        }

        Optional<AccountDTO> accountOptional
                = Optional.of(new AccountDTO(
                12533, 200, 1000.00,
                1000.00, 500, LocalDate.now(), 555,
                AccountStatus.ACTIVE, AccountType.SAVING));

        if (accountOptional.isPresent()){
            if ((clientId != accountOptional.get().getProfileId())
                    || accountOptional.get().getAccountNumber() != transactionRequested.getFromAccountNumber() ){
                throw new AccountNotBelongsToClientException(
                        "Account: " + transactionRequested.getFromAccountNumber() + " does not belong to profileId: " + clientId);
            }

            if (!(accountOptional.get().getAccountType().toString()
                    .equals(transactionRequested.getTransactionMedium().toString()))){
                throw  new AccountTypeException("Account type mismatched: Requested AccountType = " + transactionRequested.getTransactionMedium() + ",  in file AccountType = " +
                        accountOptional.get().getAccountType());
            }

        }

        return getAccountOptionalIfAccountIsActive(transactionRequested, accountOptional);
    }

    private Optional<ClientDTO> getVerifyClientByIdDetailsFromClientService(int clientId, Transaction transactionRequested) {
        // TODO -> Feign Client Call
       //return clientService.getClientById(clientId);
       Optional<ClientDTO> clientOptional = Optional.of(new ClientDTO(
               1235, "Adrian", "Paz Alonso",
               "adrianpa@gmail.com", ProfileStatus.ACTIVE));

       //client = null;

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
             var searchResult = allTransactionsByClientId.get(type)
                     .stream()
                     .filter(transaction -> transaction.getFromAccountNumber() == accountNumber)
                     .collect(Collectors.toList());
             transactionListByUserIdAndAccountId.addAll(searchResult);
             searchResult.clear();
       }

       if (transactionListByUserIdAndAccountId.size() == 0){
           throw new AccountNotPresentException("No transactions with accountNo " + accountNumber + " for user " + clientId);
       }

       var userTransactionHistoryByAccountIdDTO =  new ClientTransactionHistoryDTO();
       userTransactionHistoryByAccountIdDTO.setTransactionHistory(accountNumber, transactionListByUserIdAndAccountId);
       return userTransactionHistoryByAccountIdDTO;
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