package com.axlebank.creditCardCompany.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.service.CompanyService;
//import com.axlebank.creditCardCompany.util.exception.CompanyAlreadyExistsException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@PostMapping("/company")
	public ResponseEntity<?> registerCompany(@RequestBody Company company) {

		Company createdCompany = null;
		Optional<Company> cie = companyService.getByName(company.getCompanyName());
		if (cie.isEmpty()) {
			createdCompany = companyService.registerCompany(company);
			return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/company")
	public ResponseEntity<List<Company>> getAllCompany() {

		List<Company> companyList = companyService.getAllCompany();
		return new ResponseEntity<>(companyList, HttpStatus.OK);
	}

	@GetMapping("/company/{companyId}")
	public ResponseEntity<?> getById(@PathVariable("companyId") int companyId) {

		try {
			Company fetchedCompany = companyService.getById(companyId);
			return new ResponseEntity<>(fetchedCompany, HttpStatus.OK);

		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/company/{companyId}")
	public ResponseEntity<?> deleteCompanyById(@PathVariable("companyId") int companyId) {

		try {
			companyService.deleteCompanyById(companyId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/company/{companyId}")
	public ResponseEntity<?> updateCompany(@PathVariable("companyId") int companyId, @RequestBody Company company) {

		try {
			Company fecthedCompany = companyService.updateCompany(companyId, company);
			return new ResponseEntity<>(fecthedCompany, HttpStatus.OK);
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
