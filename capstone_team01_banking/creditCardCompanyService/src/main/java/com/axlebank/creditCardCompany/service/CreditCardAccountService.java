package com.axlebank.creditCardCompany.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.axlebank.creditCardCompany.model.CreditCardAccount;
import com.axlebank.creditCardCompany.util.exception.CompanyCCAccountNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

public interface CreditCardAccountService {

	//Create CreditCardAccount in specific Company
	public CreditCardAccount createCreditCardAccount(CreditCardAccount ccAccount, int companyId)
			throws CompanyNotFoundException, SQLIntegrityConstraintViolationException, CompanyCCProductNotFoundException;

	//Get all CreditCardAccount in specific Company
	public List<CreditCardAccount> getAllCompanyCCAccount(int companyId) throws CompanyNotFoundException;

	//Get specific CreditCardAccount By Id in specific Company
	public CreditCardAccount getCCAccountById(int companyId, int creditAccountNumber) throws CompanyNotFoundException, CompanyCCAccountNotFoundException;

	//Delete specific CreditCardAccount By id in specific Company
	public boolean deleteCompanyCCAccountById(int companyId, int creditAccountNumber)
			throws CompanyCCAccountNotFoundException, CompanyNotFoundException;

	//Update specific CreditCardAccount By Id in specific Company
	public CreditCardAccount updateCompanyCCAccountById(CreditCardAccount ccAccount, int companyId, int creditAccountNumber)
			throws CompanyNotFoundException, CompanyCCAccountNotFoundException;


}
