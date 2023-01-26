package com.axlebank.creditCardCompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.creditCardCompany.model.CreditCardAccount;

@Repository
public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, Integer> {

}
