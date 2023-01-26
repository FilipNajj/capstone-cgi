package com.axlebank.creditCardCompany.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.model.CreditCardAccount;
import com.axlebank.creditCardCompany.repository.CompanyRepository;
import com.axlebank.creditCardCompany.repository.CreditCardAccountRepository;
import com.axlebank.creditCardCompany.util.exception.CompanyCCAccountNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@Service
public class CreditCardAccountServiceImpl implements CreditCardAccountService {

	@Autowired
	private CreditCardAccountRepository ccAccountRepository;

	@Autowired
	private CompanyRepository companyRepository;

	// Create CreditCardAccount in specific Company
	@Override
	public CreditCardAccount createCreditCardAccount(CreditCardAccount ccAccount, int companyId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException {

		if (ccAccount.getCreditCardProduct() == null || ccAccount.getCreditCardProduct().getCcProductId() <= 0) {
			throw new CompanyCCProductNotFoundException();
		}

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		CreditCardAccount createdAccount = null;

		if (foundCompany.isPresent()) {

			try {
				createdAccount = ccAccountRepository.save(ccAccount);
			} catch (Exception e) {
				throw new CompanyCCProductNotFoundException();
			}
			foundCompany.get().getAccountList().add(createdAccount);
			companyRepository.save(foundCompany.get());
		} else {
			throw new CompanyNotFoundException();
		}
		return createdAccount;
	}

	// Get all CreditCardAccount in specific Company
	@Override
	public List<CreditCardAccount> getAllCompanyCCAccount(int companyId) throws CompanyNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		List<CreditCardAccount> accountList = null;

		if (foundCompany.isPresent()) {

			accountList = foundCompany.get().getAccountList();
		} else {
			throw new CompanyNotFoundException();
		}
		return accountList;
	}

	// Get specific CreditCardAccount in specific Company
	@Override
	public CreditCardAccount getCCAccountById(int companyId, int creditAccountNumber)
			throws CompanyNotFoundException, CompanyCCAccountNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		CreditCardAccount createdAccount = null;

		if (foundCompany.isPresent()) {
			try {
				createdAccount = foundCompany.get().getAccountList().stream()
						.filter(e -> e.getCreditAccountNumber() == creditAccountNumber).findFirst().get();
			} catch (NoSuchElementException e) {
				throw new CompanyCCAccountNotFoundException();
			}
		} else {
			throw new CompanyNotFoundException();
		}
		return createdAccount;
	}

	// Delete specific CreditCardAccount in specific Company
	@Override
	public boolean deleteCompanyCCAccountById(int companyId, int creditAccountNumber)
			throws CompanyCCAccountNotFoundException, CompanyNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		boolean accountDeleted = false;

		if (foundCompany.isPresent()) {

			int qteAccount = foundCompany.get().getAccountList().size();

			List<CreditCardAccount> removedAccountNumber = foundCompany.get().getAccountList().stream()
					.filter(e -> e.getCreditAccountNumber() != creditAccountNumber).collect(Collectors.toList());

			if (qteAccount == removedAccountNumber.size() + 1) {
				foundCompany.get().setAccountList(removedAccountNumber);
				companyRepository.save(foundCompany.get());
				accountDeleted = true;
			} else {
				throw new CompanyCCAccountNotFoundException();
			}
		} else {
			throw new CompanyNotFoundException();
		}
		return accountDeleted;
	}
	
	//Update specific CreditCardAccount By Id in specific Company
	@Override
	public CreditCardAccount updateCompanyCCAccountById(CreditCardAccount ccAccount, int companyId, int creditAccountNumber)
			throws CompanyNotFoundException, CompanyCCAccountNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);
		
		if (foundCompany.isPresent()) {
			try {
				foundCompany.get().getAccountList().stream().filter(e -> e.getCreditAccountNumber() == creditAccountNumber).findFirst()
						.get();
			} catch (NoSuchElementException e) {
				throw new CompanyCCAccountNotFoundException();
			}

			foundCompany.get().getAccountList().stream().filter(e -> e.getCreditAccountNumber() == creditAccountNumber).findFirst()
					.ifPresent(a -> {
						a.setExpirationDate(ccAccount.getExpirationDate());
						a.setCreditLimit(ccAccount.getCreditLimit());
						a.setUsedCredit(ccAccount.getUsedCredit());
						a.setRating(ccAccount.getRating());
						a.setCreditCardProduct(ccAccount.getCreditCardProduct());
					});

				companyRepository.save(foundCompany.get());
		} else {
			throw new CompanyNotFoundException();
		}

		return foundCompany.get().getAccountList().stream().filter(e -> e.getCreditAccountNumber() == creditAccountNumber).findFirst()
				.get();
	}
}
