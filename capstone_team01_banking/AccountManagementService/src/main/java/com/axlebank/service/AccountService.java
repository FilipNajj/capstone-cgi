package com.axlebank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.axlebank.exceptions.AccountAlreadyPresentException;
import com.axlebank.exceptions.AccountNotAvailableException;
import com.axlebank.model.Account;
import com.axlebank.model.Client;

public interface AccountService {

	List<Account> getAllAccounts();

	List<Account> getAllAccountsByProfileId(int profileId);

	Account getAccountById(int accountNumber) throws AccountNotAvailableException;

	boolean addAccount(Account account) throws AccountAlreadyPresentException;

	void deleteAccount(int accountNumber) throws AccountNotAvailableException;

	Account updateAccount(Account account) throws AccountNotAvailableException;

	boolean deleteAllAccounts();



}
