package com.axlebank.creditCardCompany.controller;

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

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.model.CreditCardProduct;
import com.axlebank.creditCardCompany.service.CreditCardProductService;
import com.axlebank.creditCardCompany.util.exception.CompanyCCProductNotFoundException;
import com.axlebank.creditCardCompany.util.exception.CompanyNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class CreditCardProductController {

	@Autowired
	private CreditCardProductService ccProductService;

	// if product was created in specific company return CREATED, if exist Before
	// create return CONFLIT
	@PostMapping("/company/product/{companyId}")
	public ResponseEntity<?> createCreditCardProduct(@RequestBody CreditCardProduct ccProduct,
			@PathVariable("companyId") int companyId) {

		try {
			CreditCardProduct createdCCProduct = ccProductService.createCreditCardProduct(ccProduct, companyId);
			return new ResponseEntity<>(createdCCProduct, HttpStatus.CREATED);

		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
	
	// if Get all Product of specific company return OK, if company not exist return
	// NOT_FIND.
	@GetMapping("/company/product")
	public ResponseEntity<?> getAllCCProduct(){

		List<CreditCardProduct> productList;
	
			productList = ccProductService.getAllCCProduct();
			return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	// if Get all Product of specific company return OK, if company not exist return
	// NOT_FIND.
	@GetMapping("/company/product/{companyId}")
	public ResponseEntity<?> getAllCompanyCCProduct(@PathVariable("companyId") int companyId) {

		List<CreditCardProduct> productList;
		try {
			productList = ccProductService.getAllCompanyCCProduct(companyId);
			return new ResponseEntity<>(productList, HttpStatus.OK);

		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// if Get product By Id of specific company return OK, if company or account not
	// exist return NOT_FIND.
	@GetMapping("/company/product/{companyId}/{ccProductId}")
	public ResponseEntity<?> getCCProductById(@PathVariable("companyId") int companyId,
			@PathVariable("ccProductId") int ccProductId) {

		try {
			CreditCardProduct productList = ccProductService.getCCProductById(companyId, ccProductId);
			return new ResponseEntity<>(productList, HttpStatus.OK);

		} catch (CompanyNotFoundException | CompanyCCProductNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// if Delete product By Id of specific company return OK, if company or account
	// not exist return NOT_FIND.
	@DeleteMapping("/company/product/{companyId}/{ccProductId}")
	public ResponseEntity<?> deleteCompanyCCProductById(@PathVariable("companyId") int companyId,
			@PathVariable("ccProductId") int ccProductId) {

		try {
			ccProductService.deleteCompanyCCProductById(companyId, ccProductId);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (CompanyNotFoundException | CompanyCCProductNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// if Update product By Id of specific company return OK, if company or account
	// not exist return NOT_FIND.
	@PutMapping("/company/product/{companyId}/{ccProductId}")
	public ResponseEntity<?> updateCompanyCCProductById(@RequestBody CreditCardProduct ccProduct,
			@PathVariable("companyId") int companyId, @PathVariable("ccProductId") int ccProductId) {

		try {
			CreditCardProduct updated = ccProductService.updateCompanyCCProductById(ccProduct, companyId, ccProductId);
			return new ResponseEntity<>(updated, HttpStatus.OK);

		} catch (CompanyNotFoundException | CompanyCCProductNotFoundException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
