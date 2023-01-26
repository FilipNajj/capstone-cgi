package com.axlebank.creditCardCompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.creditCardCompany.model.CreditCardProduct;

@Repository
public interface CreditCardProductRepository extends JpaRepository<CreditCardProduct, Integer> {

}
