package com.axlebank;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.axlebank.controller.InstitutionController;
import com.axlebank.model.Institution;
import com.axlebank.services.InstitutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class InstitutionControllerTest {
	
	
	private MockMvc mockMvc;

	@Mock
	private InstitutionService service;

	@InjectMocks
	private InstitutionController controller;

	private Institution institution1, institution2;
	private List<Institution> institutionList;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		institution1 = new Institution( "Videotron", 1514245147 ,"Electricity");
		institution2 = new Institution( "Fido", 1885475124, "Telecommunication");
		institutionList = new ArrayList<Institution>();
		institutionList.add(institution1);
		institutionList.add(institution2);
	}

	@Test
	void givenInstitutionToSaveThenShouldReturnSaveInstitution() throws JsonProcessingException, Exception {
		when(service.addInstitution(any())).thenReturn(institution1);

		mockMvc.perform(post("/api/v1/institution/save").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(institution1))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
System.out.println("---------------------------------------------------------------------");
		verify(service, times(1)).addInstitution(any());

	}

	@Test
	void getInstitutionList() throws Exception {
		when(service.getInstitutionList()).thenReturn(institutionList);
		mockMvc.perform(get("/api/v1/institution/list").contentType(MediaType.APPLICATION_JSON))
		         .andExpect(jsonPath("$[0].institutionName", Matchers.equalTo("Videotron")))
		         .andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
//				.andReturn().getResponse().getContentAsString();

		verify(service, times(1)).getInstitutionList();
	}
	
//	@Test
//	void deleteInstitution() throws Exception {
//		when(service.deleteInstitutionById(0)).thenReturn("Deleted Successfully");
//		mockMvc.perform(delete("/api/v1/institution/delete/{id}").contentType(MediaType.APPLICATION_JSON))
//		.andExpectAll(null)
//		
//	}
//	

}
