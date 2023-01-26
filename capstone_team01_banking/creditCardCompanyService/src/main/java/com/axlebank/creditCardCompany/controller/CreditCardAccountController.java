package com.axlebank.creditCardCompany.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.axlebank.creditCardCompany.model.CreditCardAccount;
import com.axlebank.creditCardCompany.service.CreditCardAccountService;
import com.axlebank.creditCardCompany.util.exception.CompanyCCAccountNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class CreditCardAccountController {

	@Autowired
	private CreditCardAccountService ccAccountService;

	// if account was created in specific company return CREATED, if exist Before create return CONFLIT
	@PostMapping("/company/account/{companyId}")
	public ResponseEntity<?> createCreditCardAccount(@RequestBody CreditCardAccount ccAccount,
			@PathVariable("companyId") int companyId) throws CompanyCCProductNotFoundException {

		try {
			CreditCardAccount createdCCAccount = ccAccountService.createCreditCardAccount(ccAccount, companyId);
			return new ResponseEntity<>(createdCCAccount, HttpStatus.CREATED);
			
		} catch (CompanyCCProductNotFoundException | CompanyNotFoundException | SQLIntegrityConstraintViolationException  e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
	
	// if Get all account of specific company return OK, if company not exist return NOT_FIND.
	@GetMapping("/company/account/{companyId}")
	public ResponseEntity<?> getAllCompanyCCAccount(@PathVariable("companyId") int companyId) {

		List<CreditCardAccount> accountList;
		try {
			accountList = ccAccountService.getAllCompanyCCAccount(companyId);
			return new ResponseEntity<>(accountList, HttpStatus.OK);
			
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// if Get account By Id of specific company return OK, if company  or account not exist return NOT_FIND.
	@GetMapping("/company/account/{companyId}/{creditAccountNumber}")
	public ResponseEntity<?> getCCAccountById(@PathVariable("companyId") int companyId,
			@PathVariable("creditAccountNumber") int creditAccountNumber) {

		try {
			CreditCardAccount createdAccount = ccAccountService.getCCAccountById(companyId, creditAccountNumber);
			return new ResponseEntity<>(createdAccount, HttpStatus.OK);

		} catch (CompanyNotFoundException | CompanyCCAccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// if Delete account By Id of specific company return OK, if company  or account not exist return NOT_FIND.
	@DeleteMapping("/company/account/{companyId}/{creditAccountNumber}")
	public ResponseEntity<?> deleteCompanyCCAccountById(@PathVariable("companyId") int companyId,
			@PathVariable("creditAccountNumber") int creditAccountNumber) {

		try {
			ccAccountService.deleteCompanyCCAccountById(companyId, creditAccountNumber);
			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (CompanyNotFoundException | CompanyCCAccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// if Update account By Id of specific company return OK, if company  or account not exist return NOT_FIND.
	@PutMapping("/company/account/{companyId}/{creditAccountNumber}")
	public ResponseEntity<?> updateCompanyCCAccountById(@RequestBody CreditCardAccount ccAccount, @PathVariable("companyId") int companyId,
			@PathVariable("creditAccountNumber") int creditAccountNumber) {
		
		try {
			CreditCardAccount updated = ccAccountService.updateCompanyCCAccountById(ccAccount, companyId, creditAccountNumber);
			return new ResponseEntity<>(updated, HttpStatus.OK);
			
		} catch (CompanyNotFoundException | CompanyCCAccountNotFoundException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
