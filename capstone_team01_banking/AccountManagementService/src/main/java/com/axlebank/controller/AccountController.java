package com.axlebank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axlebank.exceptions.AccountAlreadyPresentException;
import com.axlebank.exceptions.AccountNotAvailableException;
import com.axlebank.model.Account;
import com.axlebank.service.AccountService;


//@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// GET ALL ACCOUNTS
	@GetMapping
	public ResponseEntity<?> getAllAccounts() {
		ResponseEntity<?> responseEntity;

		List<Account> accountList = accountService.getAllAccounts();
		responseEntity = new ResponseEntity<List<Account>>(accountList, HttpStatus.OK);

		return responseEntity;
	}

	// GET ALL ACCOUNTS FOR A SPECIFIC PROFILE
	@GetMapping("/accounts-profile/{profileId}")
	public ResponseEntity<?> getAllAccountsByProfileId(@PathVariable int profileId) {
		ResponseEntity<?> responseEntity;

		List<Account> accountList = accountService.getAllAccountsByProfileId(profileId);
		responseEntity = new ResponseEntity<List<Account>>(accountList, HttpStatus.OK);

		return responseEntity;
	}

	// GET SINGLE ACCOUNT
	@GetMapping("/{accountNumber}")
	public ResponseEntity<?> getAccountById(@PathVariable int accountNumber) {
		ResponseEntity<?> responseEntity;

		try {
			Account account = accountService.getAccountById(accountNumber);
			responseEntity = new ResponseEntity<Account>(account, HttpStatus.OK);
		} catch (AccountNotAvailableException e) {
			responseEntity = new ResponseEntity<String>("Account with this id not present", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}

	// ADD ACCOUNT
	@PostMapping
	public ResponseEntity<?> addAccount(@RequestBody Account account) {
		ResponseEntity<?> responseEntity;

		try {
			boolean addedAccount = accountService.addAccount(account);
			responseEntity = new ResponseEntity<Boolean>(addedAccount, HttpStatus.CREATED);
		} catch (AccountAlreadyPresentException e) {
			responseEntity = new ResponseEntity<String>("Failed to add. Account with the id already present",
					HttpStatus.CONFLICT);
		}
		return responseEntity;
	}

	// DELETE ACCOUNT
	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<String> deleteAccount(@PathVariable int accountNumber) {
		ResponseEntity<String> responseEntity;

		try {
			accountService.deleteAccount(accountNumber);
			responseEntity = new ResponseEntity<String>("Account deleted successfully", HttpStatus.OK);
		} catch (AccountNotAvailableException e) {
			responseEntity = new ResponseEntity<String>("Account with the id not present", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}

	// UPDATE ACCOUNT
	@PutMapping("/{accountNumber}")
	public ResponseEntity<?> updateAccount(@RequestBody Account updatedAccount, @PathVariable int accountNumber) {
		ResponseEntity<?> responseEntity;		
		
		try {
			updatedAccount.setAccountNumber(accountNumber);
			Account account = accountService.updateAccount(updatedAccount);
			responseEntity = new ResponseEntity<Account>(account,HttpStatus.OK);
		} catch (AccountNotAvailableException e) {
			responseEntity = new ResponseEntity<String>("Account with the id not present", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	
	//Delete All
	@DeleteMapping
	public ResponseEntity<?> deleteAllAccounts(){
		ResponseEntity<?> responseEntity;		
		Boolean accountList = accountService.deleteAllAccounts();
		responseEntity = new ResponseEntity<Boolean>(accountList, HttpStatus.OK);

		return responseEntity;
	}
	

}
