package com.axlebank.budgettingmicroservice.utils;

import com.axlebank.budgettingmicroservice.models.Client;
import com.axlebank.budgettingmicroservice.models.Institution;
import com.axlebank.budgettingmicroservice.models.Transaction;
import com.axlebank.budgettingmicroservice.models.TransactionType;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public class Utility {

    @Autowired
    TransactionServiceCall transactionServiceCall;

    public static Institution getNotExistingInstitution() {
        Institution institution = new Institution();
        institution.setInstitutionId(-1);
        institution.setCategory("other");
        return institution;
    }

    public static boolean exists(Client client) {

        return client != null && !client.getFirstName().isEmpty()
                && !client.getLastName().isEmpty();
    }


    public Map<String, Double> dataInformationsOf(List<Transaction> transactions){


        Map<String, Double> resultData = new LinkedHashMap<>();
        // retrieve distinct institutions id;
        List<Integer> payeeIds = transactions.stream()
                .map(Transaction::getInstitutionId)
                .distinct()
                .collect(Collectors.toList());


        // retrieve distinct institutions id;
        List<String> nonRegisteredPayeesId = transactions.stream()
                .filter( transaction -> transaction.getInstitutionId() <= 0 )
                .filter(transaction -> transaction.getTransactionType() != TransactionType.DEPOSIT)
                .map(transaction -> "cash spending")
                .distinct()
                .collect(Collectors.toList());

        List<String> categories = new ArrayList<>();

        payeeIds.forEach(id ->{
            categories.add(transactionServiceCall.getPayeeNameFromPayeeID(id).getCategory());
        });
       categories.addAll(nonRegisteredPayeesId);


       categories.forEach(category->{
           final  double total = transactions.stream()
                   .filter(transaction -> isForInstitution( transaction, category))
                   .mapToDouble(Transaction::getAmount)
                   .sum();
           resultData.put(category, total);
       });

       return resultData;


    }

    private boolean isForInstitution(Transaction transaction, String category) {

        if(transaction.getInstitutionId() == 0){
            return transaction.getTransactionType() != TransactionType.DEPOSIT && category.equals("cash spending");
        } else {
            return transactionServiceCall.getPayeeNameFromPayeeID(transaction.getInstitutionId())
                    .getCategory()
                    .equalsIgnoreCase(category);
        }
    }




    /**
     * this function will return the total spent amount
     * of a client base on map of <payee id, amount spent >
     * @param data_total_spending the map containing payeeId,totalSpent
     * @return total spent of amount
     */
    public  double getTotalSpent(Map<String, Double> data_total_spending){

        Optional<Double> spendingAmount = data_total_spending
                .values()
                .stream()
                .reduce(Double::sum);
        double totalSpent = 0;
        if(spendingAmount.isPresent()){

            totalSpent = spendingAmount.get();

        }
        return totalSpent;
    }


}
