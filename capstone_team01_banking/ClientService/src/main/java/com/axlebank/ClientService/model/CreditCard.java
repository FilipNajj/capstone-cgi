package com.axlebank.ClientService.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CreditCard {
	/*
	 * - accountNumber: int (Primary key)
- accountProvider: String (text, max_50, required)
- accountType:String (text, max_50)
	- createdDate: Date;
	- balance:double
	- creditLimit:double
	- maxDailyPurchasingLimit:double
	- maxDailyWithdrawlLimit:double
	- purchasingInterestRate:double
	- cashAdvanceInterestRate:double
	- accountStatus: enum[PENDING, ACTIVE, CLOSED, SUSPENDED, FAULT,LOCKED]
	- rating:Rating
	 */
	
	
	@Id
	private int creditAccountNumber;
	private String accountProvider;
	private String accountType;
	private LocalDate createdDate = LocalDate.now();;
	private double balance;
	private double creditLimit;
	private double maxDailyPurchasingLimit;
	private double maxDailyWithdrawlLimit;
	private double purchasingInterestRate;
	private double cashAdvanceInterestRate;
	private AccountStatus accountStatus;
	private Rating rating;
	
	public CreditCard() {}
	
	public CreditCard(int creditAccountNumber, String accountProvider, String accountType, 
			double balance, double creditLimit, double maxDailyPurchasingLimit, double maxDailyWithdrawlLimit,
			double purchasingInterestRate, double cashAdvanceInterestRate, AccountStatus accountStatus, Rating rating) {
		this.creditAccountNumber = creditAccountNumber;
		this.accountProvider = accountProvider;
		this.accountType = accountType;
		this.balance = balance;
		this.creditLimit = creditLimit;
		this.maxDailyPurchasingLimit = maxDailyPurchasingLimit;
		this.maxDailyWithdrawlLimit = maxDailyWithdrawlLimit;
		this.purchasingInterestRate = purchasingInterestRate;
		this.cashAdvanceInterestRate = cashAdvanceInterestRate;
		this.accountStatus = accountStatus;
		this.rating = rating;
	}

	public int getCreditAccountNumber() {
		return creditAccountNumber;
	}

	public void setCreditAccountNumber(int creditAccountNumber) {
		this.creditAccountNumber = creditAccountNumber;
	}

	public String getAccountProvider() {
		return accountProvider;
	}

	public void setAccountProvider(String accountProvider) {
		this.accountProvider = accountProvider;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = LocalDate.now();
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public double getMaxDailyPurchasingLimit() {
		return maxDailyPurchasingLimit;
	}

	public void setMaxDailyPurchasingLimit(double maxDailyPurchasingLimit) {
		this.maxDailyPurchasingLimit = maxDailyPurchasingLimit;
	}

	public double getMaxDailyWithdrawlLimit() {
		return maxDailyWithdrawlLimit;
	}

	public void setMaxDailyWithdrawlLimit(double maxDailyWithdrawlLimit) {
		this.maxDailyWithdrawlLimit = maxDailyWithdrawlLimit;
	}

	public double getPurchasingInterestRate() {
		return purchasingInterestRate;
	}

	public void setPurchasingInterestRate(double purchasingInterestRate) {
		this.purchasingInterestRate = purchasingInterestRate;
	}

	public double getCashAdvanceInterestRate() {
		return cashAdvanceInterestRate;
	}

	public void setCashAdvanceInterestRate(double cashAdvanceInterestRate) {
		this.cashAdvanceInterestRate = cashAdvanceInterestRate;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "CreditCard [creditAccountNumber=" + creditAccountNumber + ", accountProvider=" + accountProvider
				+ ", accountType=" + accountType + ", createdDate=" + createdDate + ", balance=" + balance
				+ ", creditLimit=" + creditLimit + ", maxDailyPurchasingLimit=" + maxDailyPurchasingLimit
				+ ", maxDailyWithdrawlLimit=" + maxDailyWithdrawlLimit + ", purchasingInterestRate="
				+ purchasingInterestRate + ", cashAdvanceInterestRate=" + cashAdvanceInterestRate + ", accountStatus="
				+ accountStatus + ", rating=" + rating + "]";
	}
	
}
