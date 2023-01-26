package com.axlebank;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.axlebank.model.Institution;
import com.axlebank.repository.InstitutionRepository;
import com.axlebank.services.InstitutionServiceImpl;


// Mocking is creating mock objects that simulate the behavior of real objects

@ExtendWith(MockitoExtension.class)

// Extension that initializes mocks and handles strict stubbings

public class InstitutionServiceTest {
	
	@Mock
	private InstitutionRepository repository;
	
	@InjectMocks
	// Create an instance of the class and injects the mocks that are marked with @Mock
	private InstitutionServiceImpl service;
	
	private Institution institution1,institution2;
    private List<Institution> institutionList;
    
    @BeforeEach
    public void setUp() {
    	institution1= new Institution( "Videotron", 1514245147 , "Electricity");
    	institution2=new Institution( "Fido", 1885475124, "Telecommunication");
    	institutionList = new ArrayList<Institution>();
    	institutionList.add(institution1);
    	institutionList.add(institution2);
    }
    
    @Test
    public void gievnInstituionToSaveThenReturnSaveInstitution() {
    	// add the behavior
    	when(repository.save(any())).thenReturn(institution1);
    	
    	assertEquals(institution1.getInstitutionName(), repository.save(institution1).getInstitutionName());
    	// Verify the behavior
    	verify(repository, times(1)).save(any());
    }
    
    @Test
    public void getInstitutionList() {
    	Institution institution3= new Institution( "HydroQuebec", 1995475124, "Electricity");
    	institutionList.add(institution3);
    	when(repository.findAll()).thenReturn(institutionList);
    	List<Institution> list = repository.findAll();
    	assertEquals("HydroQuebec", list.get(2).getInstitutionName());
    	assertEquals(1995475124, list.get(2).getPhoneNumber());
    	
    	
    	verify(repository, times(1)).findAll();
    }
	
	

}
