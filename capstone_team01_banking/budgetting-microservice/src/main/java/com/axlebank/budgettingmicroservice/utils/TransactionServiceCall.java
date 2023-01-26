package com.axlebank.budgettingmicroservice.utils;

import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import com.axlebank.budgettingmicroservice.models.Client;
import com.axlebank.budgettingmicroservice.models.Institution;
import com.axlebank.budgettingmicroservice.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionServiceCall {

    private static final String GET_ALL_TRANSACTION_API_URL = "http://localhost:3000/transactions";
    @Autowired
    private WebClient.Builder webClientBuilder;

    public TransactionServiceCall() {
    }

    private String getAllTransactionUrl(int cliendId) {
        return GET_ALL_TRANSACTION_API_URL;
    }


    public List<Transaction> getAllTransactions(int clientId){


        Flux<Transaction> transactionsFlux =  webClientBuilder
                .build()
                .get()
                .uri(getAllTransactionUrl(clientId))
                .retrieve()
                .bodyToFlux(Transaction.class)
                .onErrorResume(err->{
                    return Flux.error(new Exception("client doesn't exist "+ err.getMessage() ));
                });

       return transactionsFlux.collectList().block();
    }

    public List<Transaction> getTransactionsOf( BudgetPlan budgetPlan){

        return getAllTransactions(budgetPlan.getClientId())
                .stream()
                .filter( transaction -> betweenDates(budgetPlan, transaction.getDateCreated()))
                .collect(Collectors.toList());
    }

    private boolean betweenDates(BudgetPlan budgetPlan, LocalDate dateCreated) {

        LocalDate oneDayBefore =  budgetPlan.getStartingDate().minusDays(1);
        LocalDate oneDayAfter =  budgetPlan.getEndingDate().plusDays(1);

        return dateCreated
        .isAfter(oneDayBefore)
                && dateCreated.isBefore(oneDayAfter);

    }


    public Institution getPayeeNameFromPayeeID(int payeeId){


        return webClientBuilder.build()
                .get()
              //  .uri(" /api/v1/institution/"+ payeeId)
                .uri("http://localhost:3000/institution/"+payeeId)
                .retrieve()
                .bodyToMono(Institution.class)
                .onErrorResume( err-> Mono.just( Utility.getNotExistingInstitution()))
                .block();
    }

    public boolean checkClientId(int clientId){


        try {
            return Utility.exists(webClientBuilder.build()
                    .get()
                    //  .uri(" /api/v1/institution/"+ payeeId)
                    .uri("http://localhost:3000/client/"+clientId)
                    .retrieve()
                    .bodyToMono(Client.class)
                    .block());
        } catch (Exception e){
            return false;
        }

    }


}
