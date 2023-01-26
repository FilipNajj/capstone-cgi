package com.axlebank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.model.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {

}
