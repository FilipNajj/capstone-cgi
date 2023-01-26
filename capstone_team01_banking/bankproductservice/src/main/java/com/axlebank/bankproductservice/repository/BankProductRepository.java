package com.axlebank.bankproductservice.repository;

import com.axlebank.bankproductservice.model.BankProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BankProductRepository extends JpaRepository<BankProduct, Integer> {
}
