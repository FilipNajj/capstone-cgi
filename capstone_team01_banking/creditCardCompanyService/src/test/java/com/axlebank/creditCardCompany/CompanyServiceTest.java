package com.axlebank.creditCardCompany;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.repository.CompanyRepository;
import com.axlebank.creditCardCompany.service.CompanyService;
import com.axlebank.creditCardCompany.service.CompanyServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

	@InjectMocks
	private CompanyServiceImpl companyService;

	@Mock
	private CompanyRepository companyRepository;

	private Company company1, company2;

	@BeforeEach
	public void setUp() {
		company1 = new Company();
		company1.setCompanyName("AXLeBank");
		company1.setEmail("axlebank@axlebank.com");
		company1.setPhoneNumber(438444444);

		company2 = new Company();
		company2.setCompanyName("LeBank");
		company2.setEmail("lebank@lebank.com");
		company2.setPhoneNumber(514444444);

	}

	@Test
	void givenCompanyToSaveThanReturneSavedCompany() throws JsonProcessingException, Exception {

		when(companyService.registerCompany(any())).thenReturn(company1);
		assertEquals(company1.getEmail(), companyRepository.save(company1).getEmail());
//		verify(companyRepository, times(1)).save(any());

	}

}
