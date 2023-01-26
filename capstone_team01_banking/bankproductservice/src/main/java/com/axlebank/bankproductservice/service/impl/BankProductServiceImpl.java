package com.axlebank.bankproductservice.service.impl;

import com.axlebank.bankproductservice.exception.BankProductIdAlreadyExistsException;
import com.axlebank.bankproductservice.exception.BankProductNotFoundException;
import com.axlebank.bankproductservice.model.BankProduct;
import com.axlebank.bankproductservice.repository.BankProductRepository;
import com.axlebank.bankproductservice.service.BankProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankProductServiceImpl implements BankProductService {

    @Autowired
    private BankProductRepository bankProductRepository;

    @Override
    public List<BankProduct> getAllBankProducts() {
        return bankProductRepository.findAll();
    }

    @Override
    public BankProduct getBankProducyById(int id) throws BankProductNotFoundException{
        Optional<BankProduct> optionalBankProduct = bankProductRepository.findById(id);
        if(optionalBankProduct.isEmpty()){
            throw new BankProductNotFoundException();
        }
        return optionalBankProduct.get();
    }

    @Override
    public BankProduct addBankProduct(BankProduct bankProduct) throws BankProductIdAlreadyExistsException {
        Optional<BankProduct> optionalBankProduct = bankProductRepository.findById(bankProduct.getProductId());
        if(optionalBankProduct.isPresent()){
            throw new BankProductIdAlreadyExistsException();
        }
        bankProductRepository.save(bankProduct);
        return bankProduct;
    }

    @Override
    public BankProduct editBankProduct(BankProduct bankProduct) throws BankProductNotFoundException{
        Optional<BankProduct> optionalBankProduct = bankProductRepository.findById(bankProduct.getProductId());
        if(optionalBankProduct.isEmpty()){
            throw new BankProductNotFoundException();
        }
        bankProductRepository.save(optionalBankProduct.get());
        return bankProduct;
    }

    @Override
    public boolean deleteBankProductById(int id) throws BankProductNotFoundException {
        Optional<BankProduct> optionalBankProduct = bankProductRepository.findById(id);
        if(optionalBankProduct.isEmpty()){
            throw new BankProductNotFoundException();
        }
        bankProductRepository.deleteById(id);
        return true;
    }
}
