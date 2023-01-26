package com.axlebank.transactionservice.dto;

import com.axlebank.transactionservice.model.Transaction;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Data
public class ClientTransactionHistoryDTO {
    private Map<Integer, List<Transaction>> transactionHistory = new TreeMap<>();

    public void setTransactionHistory(int accountNumber, List<Transaction> transactionListByUserIdAndAccountId) {
        this.transactionHistory.put(accountNumber, transactionListByUserIdAndAccountId);
    }
}
