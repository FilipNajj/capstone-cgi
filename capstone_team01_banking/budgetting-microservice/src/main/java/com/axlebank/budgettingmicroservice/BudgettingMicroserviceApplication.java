package com.axlebank.budgettingmicroservice;

import com.axlebank.budgettingmicroservice.models.BudgetPlan;
import com.axlebank.budgettingmicroservice.repository.BudgetPlanRepository;
import com.axlebank.budgettingmicroservice.utils.TransactionServiceCall;
import com.axlebank.budgettingmicroservice.utils.Utility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class BudgettingMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgettingMicroserviceApplication.class, args);
	}

	@Bean
	TransactionServiceCall transactionServiceCall(){
		return  new TransactionServiceCall();
	}

	@Bean
	Utility utility(){

		return  new Utility();
	}
	@Bean
	CommandLineRunner runner(BudgetPlanRepository repository){

		return args -> {
			if(repository.findAll().isEmpty()){
				BudgetPlan budgetPlan = new BudgetPlan();
				budgetPlan.setPlanId(22l);
				budgetPlan.setClientId(100);
				budgetPlan.setFixedAmount(1000);
				budgetPlan.setStartingDate(LocalDate.of(2022,3,15));
				budgetPlan.setEndingDate(budgetPlan.getStartingDate().plusMonths(1));


				BudgetPlan budgetPlan2 = new BudgetPlan();
				budgetPlan2.setPlanId(11l);
				budgetPlan2.setClientId(100);
				budgetPlan2.setFixedAmount(1500);
				budgetPlan2.setStartingDate(LocalDate.of(2022,4,15));
				budgetPlan2.setEndingDate(budgetPlan2.getStartingDate().plusMonths(1));


				BudgetPlan budgetPlan3 = new BudgetPlan();
				budgetPlan3.setPlanId(222l);
				budgetPlan3.setClientId(100);
				budgetPlan3.setFixedAmount(1000);
				budgetPlan3.setStartingDate(LocalDate.of(2022,5,15));
				budgetPlan3.setEndingDate(budgetPlan3.getStartingDate().plusMonths(1));


				repository.save(budgetPlan2);
				repository.save(budgetPlan);
				repository.save(budgetPlan3);
			}

		};
	}
}
