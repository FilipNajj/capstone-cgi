package com.axlebank.services;

import java.util.List;

import com.axlebank.exception.InstitutionNotFoundException;
import com.axlebank.model.Institution;

public interface InstitutionService {

	Institution addInstitution(Institution institution);
	
	List<Institution> getInstitutionList();
	
	Institution updateInstitution(Institution institution) throws InstitutionNotFoundException;
	
	void deleteInstitutionById(int institutionId);
	
	Institution findInstitutionById(int institutionId) throws InstitutionNotFoundException;
	
}
