package com.axlebank.creditCardCompany.service;

import java.util.List;
import java.util.Optional;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

public interface CompanyService {
	
	public Company registerCompany(Company company);

	public List<Company> getAllCompany();

	public Company getById(int companyId) throws CompanyNotFoundException;

	public boolean deleteCompanyById(int companyId) throws CompanyNotFoundException;

	public Company updateCompany(int companyId, Company company) throws CompanyNotFoundException;

	public Optional<Company> getByName(String companyName);

}
