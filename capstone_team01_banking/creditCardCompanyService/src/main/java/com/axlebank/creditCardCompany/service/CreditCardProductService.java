package com.axlebank.creditCardCompany.service;

import java.util.List;

import com.axlebank.creditCardCompany.model.CreditCardProduct;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

public interface CreditCardProductService {

	//Create CreditCardProduct in specific Company
	public CreditCardProduct createCreditCardProduct(CreditCardProduct ccProduct, int companyId)
			throws CompanyNotFoundException;

	//Get all CreditCardProduct in specific Company
	public List<CreditCardProduct> getAllCompanyCCProduct(int companyId) throws CompanyNotFoundException;

	//Get specific CreditCardProduct By Id in specific Company
	public CreditCardProduct getCCProductById(int companyId, int ccProductId) throws CompanyNotFoundException, CompanyCCProductNotFoundException;

	//Delete specific CreditCardProduct By Id in specific Company
	public boolean deleteCompanyCCProductById(int companyId, int ccProductId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException;

	//Update specific CreditCardProduct By Id in specific Company
	public CreditCardProduct updateCompanyCCProductById(CreditCardProduct ccProduct, int companyId, int ccProductId)
			throws CompanyNotFoundException, CompanyCCProductNotFoundException;

	public List<CreditCardProduct> getAllCCProduct();

}