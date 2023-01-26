package com.axlebank.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.axlebank.exception.InstitutionNotFoundException;
import com.axlebank.model.Institution;
import com.axlebank.services.InstitutionService;
import com.axlebank.services.InstitutionServiceImpl;

@RestController
@RequestMapping("api/v1/institution")
@CrossOrigin
public class InstitutionController {
	
	@GetMapping("/")
	public String test() {
		return "Axle Bank";
	}
	
	
	
	@Autowired
	private InstitutionService service;
	
	@PostMapping("/save")
	public ResponseEntity<Institution> saveInstitutionDetails(@RequestBody Institution institution){
		return new ResponseEntity<>(service.addInstitution(institution), HttpStatus.OK);
	}
    
	@GetMapping("/list")
	public ResponseEntity<?> getInstitutionList() {
		return new ResponseEntity<>(service.getInstitutionList(), HttpStatus.OK);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findInstitutionById(@PathVariable Integer id) {
		ResponseEntity<?> responseEntity;

		try {
			var inst = service.findInstitutionById(id);
			responseEntity = new ResponseEntity<>(inst, HttpStatus.OK);
		} catch (InstitutionNotFoundException e) {
			responseEntity = new ResponseEntity<>("Institution with Id: " + id + ", Not Found.", HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteInstitution(@PathVariable int id) throws InstitutionNotFoundException {
		Optional<Institution> inst = Optional.of(service.findInstitutionById(id));

		if (inst.isPresent()) {
			service.deleteInstitutionById(id);
			return "Deleted Successfully";
		} else {
			throw new InstitutionNotFoundException("Deleted Successfully");
		}

	}
	
//	@PutMapping("/update/{id}")
//	public ResponseEntity<Institution> updateEmployeeDetails(@RequestBody Institution institution,@PathVariable Integer institutionId){
//		Institution  updateInstitutionDetails = service.updateInstitution(institution, institutionId);
//		updateInstitutionDetails.setInstitutionId(institutionId);
//		
//		
//		if(updateInstitutionDetails!= null) {
//			
//			return new ResponseEntity<Object>(updateInstitutionDetails, HttpStatus.CREATED);
//		}
//		return new ResponseEntity<Object>("No Data availabe to Update", HttpStatus.NOT_FOUND);
//	}
//	
//	
//	
//	
	@PutMapping("/update/{id}")
    public ResponseEntity<Institution> updateClient(@PathVariable Integer id, @RequestBody Institution updateInstitutionDetails){
        ResponseEntity<Institution> responseEntity;
        try {
        	updateInstitutionDetails.setInstitutionId(id);
        	
        	Institution updatedInstitution = service.updateInstitution(updateInstitutionDetails);
        	
            responseEntity = new ResponseEntity<Institution>(updatedInstitution,HttpStatus.OK);
        }catch (InstitutionNotFoundException e) {
            responseEntity = new ResponseEntity<Institution>(HttpStatus.NOT_FOUND);
        }
        return null;
    }
	
}
