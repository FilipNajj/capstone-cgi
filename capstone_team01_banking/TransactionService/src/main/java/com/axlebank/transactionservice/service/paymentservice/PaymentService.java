package com.axlebank.transactionservice.service.paymentservice;

import com.axlebank.transactionservice.dto.*;
import com.axlebank.transactionservice.model.Role;
import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.service.feignclients.AccountManagementServiceClient;
import com.axlebank.transactionservice.service.feignclients.ClientService;
import com.axlebank.transactionservice.service.feignclients.InstitutionServiceClient;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.*;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotActiveException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.institutionexceptions.InstitutionNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.AmountRequestedException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.FundsException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.WithdrawalTransactionMediumException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements PaymentServiceSpec{

    private final AccountManagementServiceClient accountManagementService;
    private final ClientService clientService;

    private final InstitutionServiceClient institutionService;

    public PaymentService(AccountManagementServiceClient accountManagementService, ClientService clientService, InstitutionServiceClient institutionService) {
        this.accountManagementService = accountManagementService;
        this.clientService = clientService;
        this.institutionService = institutionService;
    }

    @Override
    public List<Transaction> makePayment(Transaction transactionRequested, int clientId) {
        List<Transaction> transactionsProcessedList = new ArrayList<>();
        verifyIfInstitutionExistAtInstitutionService(transactionRequested.getInstitutionId(), transactionRequested);

        var amountRequested = verifyIsDeductionTransactionAndGetAmount(transactionRequested);
        getVerifyClientByIdDetailsFromClientService(clientId, transactionRequested);
        var fromAccountDTO = getAccountDetailsFromAccountManagementService(clientId, transactionRequested, transactionRequested.getFromAccountNumber());

        if (fromAccountDTO.isEmpty()){
            throw new AccountNotPresentException("Cannot process " + transactionRequested.getTransactionType() + ", accountNo: " + transactionRequested.getFromAccountNumber() + " does not exist.");
        }

        double availableFundsInAccount
                = verifyAvailableFundsAndGetBalanceInAccount(transactionRequested,fromAccountDTO);

        fromAccountDTO.get().setBalance(availableFundsInAccount + amountRequested);

        var fromAccountDTOUpdatedOptional =updateAccountDetailsInAccountService(fromAccountDTO.get(), fromAccountDTO.get().getAccountNumber(), transactionRequested);

        boolean isAccountUpdated = false;

        if(fromAccountDTOUpdatedOptional.isPresent()){
            if ((fromAccountDTOUpdatedOptional.get().getBalance()) == availableFundsInAccount - (amountRequested * -1)){
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
        return transactionsProcessedList;
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

    private double verifyIsDeductionTransactionAndGetAmount(Transaction transactionRequested) {
        var amountRequested = transactionRequested.getAmount();

        if (amountRequested > 0 || amountRequested == 0){
            throw new AmountRequestedException(
                    transactionRequested.getTransactionType() +
                            " is a minus operation please ensure amount: is less than zero eg. -150");

        }
        return amountRequested;
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

    private double verifyAvailableFundsAndGetBalanceInAccount(Transaction transactionRequested, Optional<AccountDTO> fromAccountDTO) {
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
}
