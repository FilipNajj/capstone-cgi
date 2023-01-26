package com.axlebank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.axlebank.model.Institution;
import com.axlebank.repository.InstitutionRepository;

@SpringBootTest
public class InstitutionRepositoryTest {
	
	@Autowired
	private InstitutionRepository repository;
	
	private Institution institution1, institution2;
	
	@BeforeEach
	public void setUp() {
		institution1=new Institution( "Videotron", 1514245147, "Electricity");
		institution2=new Institution( "Fido", 1885475124, "Telecommunication");
	}
	
	@Test
	public void givenInstitutionToSaveThenReturnSavedInstitution() {
		Institution inst1 = repository.save(institution1);
		assertEquals(institution1.getInstitutionName(), inst1.getInstitutionName()); 
		Institution save = repository.save(institution2);
		System.out.println("-----------" + save + "----------");
		Institution inst2=repository.findById(save.getInstitutionId()).get();
		assertEquals("Fido", inst2.getInstitutionName());
		
	}
	
	@DisplayName("Test for Get Institution List")
	@Test
	public void getAllInstitutionList() {
		repository.save(institution1);
		repository.save(institution2);
		List<Institution> list = repository.findAll();
		assertEquals("Videotron", list.get(0).getInstitutionName());
	}
	
	@AfterEach
	public void tearDown() {
		repository.deleteAll();
	}

}
