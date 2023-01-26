package com.axlebank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.axlebank.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axlebank.FeignClient.FeignService;
import com.axlebank.model.Account;
import com.axlebank.model.AccountStatus;
import com.axlebank.model.Client;
import com.axlebank.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FeignService feignService;
	
	//GET ALL ACCOUNTS
	@Override
	public List<Account> getAllAccounts(){
		return accountRepository.findAll();
	}

	
	//GET ALL ACCOUNTS FOR A SPECIFIC PROFILE
	@Override
	public List<Account> getAllAccountsByProfileId(int profileId) {
		return accountRepository.findAllByProfileId(profileId);
	}
	
	
	//GET SINGLE ACCOUNT
	@Override
	public Account getAccountById(int accountNumber) throws AccountNotAvailableException {
		Optional<Account> optional = accountRepository.findById(accountNumber);
		
		if (optional.isPresent()) {
			return optional.get();
		}
	
		throw new AccountNotAvailableException();
	}

	
	//ADD ACCOUNT
	@Override
	public boolean addAccount(Account account) throws AccountAlreadyPresentException {
			int profileId = account.getProfileId();
			Optional<Client> clientOptional;
			
			try {
				clientOptional = Optional.ofNullable(feignService.getClientById(profileId));
			} catch (Exception e) {
		         throw new ClientNoPresentException("Client with ID: " + account.getProfileId() + " does not exist.");
			}

			Client client = clientOptional.orElseThrow(() -> new ClientNoPresentException("Client with ID: " + account.getProfileId() + " does not exist."));

			List<Account> accountsList = client.getAccounts();

			accountRepository.save(account);
			accountsList.add(account);
			client.setAccounts(accountsList);

			try {
				feignService.updateClient(profileId, client);
			}catch (Exception feignE){
				throw new FeignClientServiceException(feignE.getMessage());
			}

			return true;
	}

	
	//DELETE ACCOUNT
	@Override
	public void deleteAccount(int accountNumber) throws AccountNotAvailableException {
		Optional<Account> optional = accountRepository.findById(accountNumber);
		
			Account account = optional.orElseThrow(AccountNotAvailableException::new);
			accountRepository.delete(account);
			int profileId = account.getProfileId();

			Optional<Client> clientOptional;
			
			try {
				clientOptional = Optional.ofNullable(feignService.getClientById(profileId));
			} catch (Exception e) {
		         throw new ClientNoPresentException(e.getMessage());
			}
			
			Client client = clientOptional.orElseThrow(() -> new ClientNoPresentException("Client with ID: " + account.getProfileId() + " does not exist."));

			List<Account> accountsList;
			
			try {
				accountsList = client.getAccounts();
				accountsList.removeIf(acc -> acc.getAccountNumber()== accountNumber);
				client.setAccounts(accountsList);
			} catch (Exception e) {
				throw new AccountNotAvailableException();
			}
		
			try {
				feignService.updateClient(profileId, client);
			}catch (Exception feignE){
				throw new FeignClientServiceException(feignE.getMessage());
			}				
	}

	
	//UPDATE ACCOUNT
	@Override
	public Account updateAccount(Account account) throws AccountNotAvailableException {
		Optional<Account> optional = accountRepository.findById(account.getAccountNumber());
		
		if (optional.isPresent()) {
			int profileId = account.getProfileId();
			Optional<Client> clientOptional;
			
			try {
				clientOptional = Optional.ofNullable(feignService.getClientById(profileId));
			} catch (Exception e) {
		         throw new ClientNoPresentException("Client with ID: " + account.getProfileId() + " does not exist.");
			}

			Client client = clientOptional.orElseThrow(() -> new ClientNoPresentException("Client with ID: " + account.getProfileId() + " does not exist."));

			Optional<List<Account>> accountsListOptional = Optional.ofNullable(client.getAccounts());
			
			List<Account> accountList = accountsListOptional.orElseThrow(
					()->new NoAccountsForClientException("No Accounts for client Id: " + client.getProfileId()));
			
//			if (accountsListOptional.isEmpty()){
//				throw new NoAccountsForClientException("No Accounts for client Id: " + client.getProfileId());
//			}
//
//			List<Account> accountList = accountsListOptional.get();

			if(accountList.stream().anyMatch(n-> n.getAccountNumber() == account.getAccountNumber())) {
				accountRepository.save(account);
				List<Account> updatedAccountsList = new ArrayList<Account>();
				
				for(Account acc: accountsListOptional.get()) {
					if (acc.getAccountNumber()==account.getAccountNumber()) {
						acc= account;
					}
					updatedAccountsList.add(acc);
				}
				
				client.setAccounts(updatedAccountsList);

				try {
					feignService.updateClient(profileId, client);
				}catch (Exception feignE){
					throw new FeignClientServiceException(feignE.getMessage());
				}
				
				return account;
			}	
			throw new AccountNotAvailableException();
		}
		throw new AccountNotAvailableException();
	}


	@Override
	public boolean deleteAllAccounts() {
		accountRepository.deleteAll();
		return true;
	}
}




























