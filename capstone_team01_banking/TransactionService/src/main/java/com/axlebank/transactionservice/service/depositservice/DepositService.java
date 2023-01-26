package com.axlebank.transactionservice.service.depositservice;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.Role;
import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.model.TransactionMedium;
import com.axlebank.transactionservice.model.TransactionType;
import com.axlebank.transactionservice.service.feignclients.AccountManagementServiceClient;
import com.axlebank.transactionservice.service.feignclients.ClientService;

import com.axlebank.transactionservice.service.feignclients.InstitutionServiceClient;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountNotActiveException;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountNotBelongsToClientException;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountNotPresentException;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountTypeException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotActiveException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.institutionexceptions.InstitutionNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.FundsException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.WithdrawalTransactionMediumException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepositService implements  DepositServiceSpec{

    private final ClientService clientService;
    private final AccountManagementServiceClient accountManagementService;

    private final InstitutionServiceClient institutionService;

    public DepositService(ClientService clientService, AccountManagementServiceClient accountManagementService, InstitutionServiceClient institutionService) {
        this.clientService = clientService;
        this.accountManagementService = accountManagementService;
        this.institutionService = institutionService;
    }

    @Override
    public List<Transaction> makeDeposit(Transaction transactionRequested, int clientId) {
        List<Transaction> transactionsProcessedList = new ArrayList<>();
        var amountReceived =  transactionRequested.getAmount();

        if (amountReceived <= 0){
            throw new FundsException("Funds amount must be greater than $0.00");
        }

        getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);
        Optional<AccountDTO> toAccountDTO =
                getAccountDetailsFromAccountManagementService(clientId, transactionRequested, transactionRequested.getToAccountNumber());

        if (transactionRequested.getTransactionMedium().equals(TransactionMedium.PAYROLL)){
            verifyIfInstitutionExistAtInstitutionService(transactionRequested.getInstitutionId(), transactionRequested);
        }

        boolean isDeposited = false;

        if (toAccountDTO.isPresent()){
            var toAccountBalance = toAccountDTO.get().getBalance();
            toAccountDTO.get().setBalance(toAccountBalance + amountReceived);

            var toAccountDTOUpdated
                    = updateAccountDetailsInAccountService(toAccountDTO.get(), toAccountDTO.get().getAccountNumber(), transactionRequested);

            if (toAccountDTOUpdated.isEmpty()){
                throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
            }

            isDeposited =  (toAccountDTOUpdated.get().getBalance() == toAccountDTO.get().getBalance() &&
                    toAccountDTOUpdated.get().getAccountNumber() == toAccountDTO.get().getAccountNumber());
        }

        var isTransactionMediumCorrect = false;
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
                    isTransactionMediumCorrect = true;
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
                    isTransactionMediumCorrect = true;
                }
                break;
            }
        }

        if (!isTransactionMediumCorrect){
            throw new WithdrawalTransactionMediumException("Deposit can only be made using: PAYROLL, CHEQUE OR CASH");
        }

        return transactionsProcessedList;
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


    private Optional<AccountDTO> getAccountDetailsFromAccountManagementService(int clientId, Transaction transactionRequested, int account) {
        Optional<AccountDTO> accountOptional;

        try{
            accountOptional
                    = Optional.ofNullable(accountManagementService.getAccountByIds(account));
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + account + " does not exist.");
        }

        if (accountOptional.isPresent()){
            if (clientId != accountOptional.get().getProfileId()){
                if (accountOptional.get().getAccountNumber() != transactionRequested.getFromAccountNumber()){
                    throw new AccountNotBelongsToClientException(
                            "Account: " + transactionRequested.getToAccountNumber() + " does not belong to profileId: " + clientId);
                }
            }

            if (!transactionRequested.getTransactionType().toString().equals(TransactionType.DEPOSIT.toString()) &&
                    !transactionRequested.getTransactionType().toString().equals(TransactionType.WITHDRAWAL.toString())
            ){
                if (!(accountOptional.get().getAccountType().toString()
                        .equals(transactionRequested.getTransactionMedium().toString()))){
                    throw  new AccountTypeException("Account type mismatched: Requested AccountType = " + transactionRequested.getTransactionMedium() + ",  in file AccountType = " +
                            accountOptional.get().getAccountType());
                }
            }
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

    private void verifyIfInstitutionExistAtInstitutionService(int institutionId, Transaction transactionRequested) {
        Optional<InstitutionDTO> institutionDTOOptional;

        try{
            institutionDTOOptional = Optional.ofNullable(institutionService.findInstitutionById(institutionId));
        } catch (Exception e) {
            throw new InstitutionNotPresentException("Failed to retrieve Institution Message: " + e.getMessage());
        }

        if (institutionDTOOptional.isEmpty()){
            throw new InstitutionNotPresentException("Cannot process " +
                    transactionRequested.getTransactionType() +
                    " , Reason: institutionNo " +
                    institutionId +  " does not exist, please contact one of our admin representative");
        }
    }

    private Optional<AccountDTO> updateAccountDetailsInAccountService(AccountDTO accountDTO, int accountNumber, Transaction transactionRequested) {
        AccountDTO accountOptional;
        try{
            accountOptional =  accountManagementService.updateAccount(accountDTO, accountNumber);
        } catch (Exception e) {
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", --> Detail message: " + e.getMessage());
        }
        return Optional.ofNullable(accountOptional);
    }




}
