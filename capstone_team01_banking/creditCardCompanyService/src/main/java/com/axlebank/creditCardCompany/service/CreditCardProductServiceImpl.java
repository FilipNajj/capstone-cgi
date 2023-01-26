package com.axlebank.creditCardCompany.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.model.CreditCardProduct;
import com.axlebank.creditCardCompany.repository.CompanyRepository;
import com.axlebank.creditCardCompany.repository.CreditCardProductRepository;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@Service
public class CreditCardProductServiceImpl implements CreditCardProductService {

	@Autowired
	private CreditCardProductRepository ccProductRepository;

	@Autowired
	private CompanyRepository companyRepository;

	// Create CreditCardProduct in specific Company
	@Override
	public CreditCardProduct createCreditCardProduct(CreditCardProduct ccProduct, int companyId)
			throws CompanyNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		CreditCardProduct createdProduct = null;

		if (foundCompany.isPresent()) {

			createdProduct = ccProductRepository.save(ccProduct);
			foundCompany.get().getProductsList().add(createdProduct);
			companyRepository.save(foundCompany.get());
		} else {
			throw new CompanyNotFoundException();
		}
		return createdProduct;
	}

	// Get all CreditCardProduct in specific Company
	@Override
	public List<CreditCardProduct> getAllCCProduct() {
		List<Company> companyList = companyRepository.findAll();
		List<CreditCardProduct> ccProductList = new ArrayList<>();
		for (Company c : companyList) {
			if (c.getProductsList() != null) {
				ccProductList.addAll(c.getProductsList());
			}
		}

		return ccProductList;
	}

	// Get all CreditCardProduct in specific Company
	@Override
	public List<CreditCardProduct> getAllCompanyCCProduct(int companyId) throws CompanyNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		List<CreditCardProduct> productList = null;

		if (foundCompany.isPresent()) {

			productList = foundCompany.get().getProductsList();
		} else {
			throw new CompanyNotFoundException();
		}
		return productList;
	}

	// Get specific CreditCardProduct By Id in specific Company
	@Override
	public CreditCardProduct getCCProductById(int companyId, int ccProductId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		CreditCardProduct createdProduct = null;

		if (foundCompany.isPresent()) {
			try {
				createdProduct = foundCompany.get().getProductsList().stream()
						.filter(e -> e.getCcProductId() == ccProductId).findFirst().get();
			} catch (NoSuchElementException e) {
				throw new CompanyCCProductNotFoundException();
			}
		} else {
			throw new CompanyNotFoundException();
		}
		return createdProduct;
	}

	// Delete specific CreditCardProduct By Id in specific Company
	@Override
	public boolean deleteCompanyCCProductById(int companyId, int ccProductId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		boolean productDeleted = false;

		if (foundCompany.isPresent()) {

			int qteProducts = foundCompany.get().getProductsList().size();

			List<CreditCardProduct> removedProductId = foundCompany.get().getProductsList().stream()
					.filter(e -> e.getCcProductId() != ccProductId).collect(Collectors.toList());

			if (qteProducts == removedProductId.size() + 1) {
				foundCompany.get().setProductsList(removedProductId);
				companyRepository.save(foundCompany.get());
				productDeleted = true;
			} else {
				throw new CompanyCCProductNotFoundException();
			}
		} else {
			throw new CompanyNotFoundException();
		}
		return productDeleted;
	}

	// Update specific CreditCardProduct By Id in specific Company
	@Transactional
	@Override
	public CreditCardProduct updateCompanyCCProductById(CreditCardProduct ccProduct, int companyId, int ccProductId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException {

		Optional<Company> foundCompany = companyRepository.findById(companyId);

		if (foundCompany.isPresent()) {
			try {
				foundCompany.get().getProductsList().stream().filter(e -> e.getCcProductId() == ccProductId).findFirst()
						.get();
			} catch (NoSuchElementException e) {
				throw new CompanyCCProductNotFoundException();
			}

			foundCompany.get().getProductsList().stream().filter(e -> e.getCcProductId() == ccProductId).findFirst()
					.ifPresent(p -> {
						p.setName(ccProduct.getName());
						p.setType(ccProduct.getType());
						p.setDescription(ccProduct.getDescription());
						p.setStartingDate(ccProduct.getStartingDate());
						p.setEndingDate(ccProduct.getEndingDate());
						p.setMonthsToExpire(ccProduct.getMonthsToExpire());
						p.setCashAdvanceInterestRate(ccProduct.getCashAdvanceInterestRate());
						p.setPurchaseInterestRate(ccProduct.getPurchaseInterestRate());
						p.setAverageRating(ccProduct.getAverageRating());
					});

			companyRepository.save(foundCompany.get());
		} else {
			throw new CompanyNotFoundException();
		}

		return foundCompany.get().getProductsList().stream().filter(e -> e.getCcProductId() == ccProductId).findFirst()
				.get();
	}

}
