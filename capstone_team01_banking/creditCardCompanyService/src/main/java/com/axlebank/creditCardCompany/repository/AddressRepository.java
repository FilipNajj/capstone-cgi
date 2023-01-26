package com.axlebank.creditCardCompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.creditCardCompany.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
