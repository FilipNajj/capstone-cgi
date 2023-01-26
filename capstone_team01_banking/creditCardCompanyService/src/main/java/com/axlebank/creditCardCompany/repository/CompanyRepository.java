package com.axlebank.creditCardCompany.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.creditCardCompany.model.Company;

@Repository
@Transactional
public interface CompanyRepository extends JpaRepository<Company, Integer>{

	void save(Optional<Company> foundCompany);

	Optional<Company> findByCompanyName(String companyName);

}
