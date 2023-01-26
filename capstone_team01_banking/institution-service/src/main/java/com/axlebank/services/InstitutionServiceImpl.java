package com.axlebank.services;

import java.util.List;
import java.util.Optional;

import com.axlebank.exception.InstitutionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axlebank.model.Institution;
import com.axlebank.repository.InstitutionRepository;

@Service
public class InstitutionServiceImpl implements InstitutionService {

	@Autowired
	private InstitutionRepository repository;

	@Override
	public Institution addInstitution(Institution institution) {
		// TODO Auto-generated method stub
		return repository.save(institution);
	}

	@Override
	public List<Institution> getInstitutionList() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Institution updateInstitution(Institution updateInstitution) {
		// TODO Auto-generated method stub

		Optional<Institution> searchInstitution = repository.findById(updateInstitution.getInstitutionId());
		if (searchInstitution.isPresent()) {
			return repository.save(updateInstitution);
		}
		return null;
	}

	@Override
	public void deleteInstitutionById(int institutionId) {
		// TODO Auto-generated method stub
            repository.deleteById(institutionId);
	}

	@Override
	public Institution findInstitutionById(int institutionId) throws InstitutionNotFoundException {
		// TODO Auto-generated method stub

		// Check Institution if exist first

		Optional<Institution> institutionOptional = repository.findById(institutionId);

		if (institutionOptional.isEmpty()){
			throw new InstitutionNotFoundException("Institution not found");
		}

		return  institutionOptional.get();

//		Optional<Institution> inst = repository.findById(institutionId);
//		if (inst.isPresent()) {
//		return inst.get();}
//		return null;
	}
	
	

}
