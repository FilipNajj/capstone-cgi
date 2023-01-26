package com.axlebank.bankproductservice.controller;

import com.axlebank.bankproductservice.exception.BankProductIdAlreadyExistsException;
import com.axlebank.bankproductservice.exception.BankProductNotFoundException;
import com.axlebank.bankproductservice.model.BankProduct;
import com.axlebank.bankproductservice.service.BankProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BankProductController {

    @Autowired
    private BankProductService bankProductService;

    @GetMapping("/bankproducts")
    public ResponseEntity<List<BankProduct>> getAllBankProductsHandler() {
        return ResponseEntity.ok(bankProductService.getAllBankProducts());
    }

    @GetMapping("/bankproducts/{bankProductId}")
    public ResponseEntity<?> getBankProductById(@PathVariable("bankProductId") int bankProductId) {
        try {
            BankProduct bankProduct = bankProductService.getBankProducyById(bankProductId);
            return new ResponseEntity<BankProduct>(bankProduct, HttpStatus.OK);
        } catch (BankProductNotFoundException e) {
            return new ResponseEntity<>("No Bank Products found with this ID.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bankproducts")
    public ResponseEntity<?> addNewBankProductHandler(@RequestBody BankProduct bankProduct) {
        try {
            bankProductService.addBankProduct(bankProduct);
            return new ResponseEntity<>(bankProduct, HttpStatus.CREATED);
        } catch (BankProductIdAlreadyExistsException e) {
            return new ResponseEntity<String>("A Bank Product with this ID already exists.", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/bankproducts")
    public ResponseEntity<?> editBankProductDetailsHandler(@RequestBody BankProduct bankProduct) {
        try {
            bankProductService.editBankProduct(bankProduct);
            return new ResponseEntity<>(bankProduct, HttpStatus.OK);
        } catch (BankProductNotFoundException e) {
            return new ResponseEntity<>("No Bank Products found with this ID.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/bankproducts/{bankProductId}")
    public ResponseEntity<String> deleteBankProductByIdHandler(@PathVariable("bankProductId") int bankProductId) {
        try {
            bankProductService.deleteBankProductById(bankProductId);
            return new ResponseEntity<>("Bank Product deleted successfully.", HttpStatus.OK);
        } catch (BankProductNotFoundException e) {
            return new ResponseEntity<>("No Bank Products found with this ID.", HttpStatus.NOT_FOUND);
        }
    }
}
