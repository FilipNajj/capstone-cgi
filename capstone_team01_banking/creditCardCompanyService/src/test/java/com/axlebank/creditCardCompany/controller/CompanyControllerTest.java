package com.axlebank.creditCardCompany.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.axlebank.creditCardCompany.model.Company;
import com.axlebank.creditCardCompany.service.CompanyService;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private CompanyController companyController;

	@Mock
	private CompanyService companyService;

	private Company company1, company2;

	@BeforeEach
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();

//		company1 = new Company();
//		company1.setCompanyName("AXLeBank");
//		company1.setEmail("axlebank@axlebank.com");
//		company1.setPhoneNumber(438444444);
//
//		company2 = new Company();
//		company2.setCompanyName("LeBank");
//		company2.setEmail("lebank@lebank.com");
//		company2.setPhoneNumber(514444444);

	}

//	@Test
//	public void givenCompanyToSaveThanReturneSavedCompany() throws JsonProcessingException, Exception {
//		
//		when(companyService.registerCompany(any())).thenReturn(company1);
//		mockMvc.perform(post("/api/v1/company").contentType(MediaType.APPLICATION_JSON)
//				.content(new ObjectMapper().writeValueAsString(company1))).andExpect(status().isCreated())
//		.andDo(MockMvcResultHandlers.print());
//		
//	}

//	@Test
//	public void givenGetRequestToCompanyThenReturnAllCompany() throws Exception {
//		Address addresseOne = new Address("Rue One", 100, "Toronto", "h1h3t3");
//		CreditCardProduct ccproductOne = new CreditCardProduct("Visa Travel", "Visa", );
//		
//		 List<CreditCardProduct> productList = new ArrayList<CreditCardProduct>(6);
//		 List<CreditCardAccount> accountList = new ArrayList<CreditCardAccount>(8);
//		
//		Company companyOne = new Company("Bank", addresseOne, "bank@bank.com", "2022-08-26", 438444444, productOne, accountOne, "password");
//		
//		Mockito.when(companyService.getAllCompany()).thenReturn(companyList);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/company"))
//		.andDo(MockMvcResultHandlers.print());
//	}

}