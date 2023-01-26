package com.axlebank.transactionservice.controller;

import com.axlebank.transactionservice.model.Role;
import com.axlebank.transactionservice.model.Transaction;
import com.axlebank.transactionservice.service.TransactionServiceSpec;
import com.axlebank.transactionservice.dto.ClientTransactionHistoryDTO;
import com.axlebank.transactionservice.util.exceptions.accountexceptions.AccountNotPresentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;



@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final TransactionServiceSpec transactionService;

    @Autowired
    public TransactionController(TransactionServiceSpec transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{clientId}")
    public ResponseEntity<?> newTransactionHandler(
            @PathVariable("clientId") int clientId,
            @RequestBody Transaction transactionRequested){
        transactionRequested.setTransactionId(transactionRequested.getTransactionType(), clientId);
        transactionRequested.setCreatedBy((transactionRequested.getAdminId() == 0)? Role.CLIENT: Role.ADMIN);
        transactionRequested.setClientId(clientId);

        var newTransaction = transactionService.newTransaction(transactionRequested, clientId);
        return (newTransaction != null)?
                new ResponseEntity<>(newTransaction, HttpStatus.CREATED)
                : new ResponseEntity<>("added successfully", HttpStatus.CONFLICT);
    }

    @GetMapping("/{clientId}/{transactionId}")
    public ResponseEntity<?> getTransactionByUserIdAndTransactionIdHandler(
            @PathVariable("clientId") int clientId,
            @PathVariable("transactionId") String transactionId){
        Transaction transaction = transactionService.getTransactionByClientIdAndTransactionId(clientId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @DeleteMapping("/{clientId}/{transactionId}")
    public ResponseEntity<String> deleteTransactionByUserIdAndTransactionIdHandler(
            @PathVariable("clientId") int clientId, @PathVariable("transactionId") String transactionId){
        boolean status = transactionService.deleteTransactionByUserIdAndTransactionId(clientId, transactionId);
        return status?
                new ResponseEntity<>("Transaction " +  transactionId + " was deleted successfully", HttpStatus.OK):
                new ResponseEntity<>("Transaction " +  transactionId + " was deleted successfully", HttpStatus.CONFLICT);
    }

    @GetMapping("/types/{clientId}")
    public ResponseEntity<?> getAllTransactionsByClientIdTypesAHandler(@PathVariable("clientId") int clientId){
        var transactionsByClientId = transactionService.getAllTransactionsMapByClientId(clientId);
        return new ResponseEntity<>(transactionsByClientId, HttpStatus.OK);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> getAllTransactionsByClientIdAHandler(@PathVariable("clientId") int clientId){
        List<Transaction> transactionList = transactionService.getAllTransactionsListByClientId(clientId);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }


    @GetMapping("/account/{accountNumber}/{clientId}")
    public ResponseEntity<ClientTransactionHistoryDTO> getAllTransactionsByClientIdAndByAccountIdHandler(
            @PathVariable("clientId") int clientId,
            @PathVariable("accountNumber") int accountNumber) throws AccountNotPresentException {
        ClientTransactionHistoryDTO clientTransactionHistoryDTO
                = transactionService.getAllTransactionsByClientIdAndByAccountIdHandler(clientId, accountNumber);
        return new ResponseEntity<>(clientTransactionHistoryDTO, HttpStatus.OK);

    }

    @GetMapping("/after/{clientId}")
    public ResponseEntity<?> getAllTransactionsByClientIdAfterDateAHandler (
            @PathVariable("clientId") int clientId,
            @RequestParam("date") String dateCreated) throws DateTimeParseException {

        var date = LocalDate.parse(dateCreated, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Transaction> transactionList
                = transactionService.getAllTransactionsByClientIdAfterDate(clientId, date);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping("/before/{clientId}")
    public ResponseEntity<?> getAllTransactionsByClientIdBeforeDateAHandler (
            @PathVariable("clientId") int clientId,
            @RequestParam("date") String dateCreated) throws DateTimeParseException {

        var date = LocalDate.parse(dateCreated, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Transaction> transactionList
                = transactionService.getAllTransactionsByClientIdBeforeDate(clientId, date);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
}
