package com.axlebank.creditCardCompany.service;

import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Optional;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

//import com.axlebank.creditCardCompany.model.Address;
import com.axlebank.creditCardCompany.model.Company;
//import com.axlebank.creditCardCompany.repository.AddressRepository;
import com.axlebank.creditCardCompany.repository.CompanyRepository;
//import com.axlebank.creditCardCompany.util.exception.CompanyAlreadyExistsException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

//	@Autowired
//	private AddressRepository addressRepository;
	@Override
	public Company registerCompany(Company company) {
//		Address createdAddress = addressRepository.save(company.getAddress());

		Company createdCompany = companyRepository.save(company);

		return createdCompany;
	}
	
	@Override
	public Optional<Company> getByName(String companyName) {
	
		return companyRepository.findByCompanyName(companyName);
	}

	@Override
	public List<Company> getAllCompany() {

		return companyRepository.findAll();
	}

	@Override
	public Company getById(int companyId) throws CompanyNotFoundException {

		Company fecthedCompany = null;
		try {
			fecthedCompany = companyRepository.findById(companyId).get();

		} catch (NoSuchElementException e) {
			throw new CompanyNotFoundException();
		}
		return fecthedCompany;
	}

	@Override
	public boolean deleteCompanyById(int companyId) throws CompanyNotFoundException {

		boolean companyDeleted = false;
		try {
			companyRepository.deleteById(companyId);
			companyDeleted = true;

		} catch (EmptyResultDataAccessException e) {
			throw new CompanyNotFoundException();
		}
		return companyDeleted;
	}

	@Override
	public Company updateCompany(int companyId, Company company) throws CompanyNotFoundException {

		try {
			Company fecthedCompany = companyRepository.findById(companyId).get();
			fecthedCompany.setCompanyName(company.getCompanyName());
			fecthedCompany.setAddress(company.getAddress());
			fecthedCompany.setEmail(company.getEmail());
			fecthedCompany.setPhoneNumber(company.getPhoneNumber());
			fecthedCompany.setProductsList(company.getProductsList());
			fecthedCompany.setAccountList(company.getAccountList());

			companyRepository.save(fecthedCompany);
			return fecthedCompany;

		} catch (NoSuchElementException e) {
			throw new CompanyNotFoundException();
		}
	}


}
