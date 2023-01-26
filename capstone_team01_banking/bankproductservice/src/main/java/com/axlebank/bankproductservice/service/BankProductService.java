package com.axlebank.bankproductservice.service;

import com.axlebank.bankproductservice.exception.BankProductIdAlreadyExistsException;
import com.axlebank.bankproductservice.exception.BankProductNotFoundException;
import com.axlebank.bankproductservice.model.BankProduct;

import java.util.List;

public interface BankProductService {
    List<BankProduct> getAllBankProducts();
    BankProduct getBankProducyById(int id) throws BankProductNotFoundException;
    BankProduct addBankProduct(BankProduct bankProduct) throws BankProductIdAlreadyExistsException;
    BankProduct editBankProduct(BankProduct bankProduct) throws BankProductNotFoundException;
    boolean deleteBankProductById(int id) throws BankProductNotFoundException;
}
