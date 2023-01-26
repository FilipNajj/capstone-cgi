package com.axlebank.ClientService.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Account {
	/*
	 * - accountNumber: int (Primary key)
	- createdDate: Date;
	- accountType: enum[CHECKING, SAVING]
	- balance:double
	- maxDailyPurchasingLimit:double
	- maxDailyWithdrawlLimit:double
	- interestRate:double
	- accountStatus: enum[PENDING, ACTIVE, CLOSED, SUSPENDED, FAULT,LOCKED]
	- overDraft:double+
	 */
	
	
	@Id
	private int accountNumber;
	private LocalDate createdDate = LocalDate.now();;
	private AccountType accountType;
	private double balance;
	private double maxDailyPurchasingLimit;
	private double maxDailyWithdrawlLimit;
	private double interestRate;
	private AccountStatus accountStatus;
	private double overDraft;
	private int profileId;
	
	public Account() {}
	
	public Account(int accountNumber, LocalDate createdDate, AccountType accountType, double balance,
			double maxDailyPurchasingLimit, double maxDailyWithdrawlLimit, double interestRate,
			AccountStatus accountStatus, double overDraft, int profileId) {
		this.accountNumber = accountNumber;
		this.createdDate = createdDate;
		this.accountType = accountType;
		this.balance = balance;
		this.maxDailyPurchasingLimit = maxDailyPurchasingLimit;
		this.maxDailyWithdrawlLimit = maxDailyWithdrawlLimit;
		this.interestRate = interestRate;
		this.accountStatus = accountStatus;
		this.overDraft = overDraft;
		this.profileId = profileId;
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = LocalDate.now();
	}
	
	public AccountType getAccountType() {
		return accountType;
	}
	
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
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
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public double getOverDraft() {
		return overDraft;
	}
	
	public void setOverDraft(double overDraft) {
		this.overDraft = overDraft;
	}


	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	
	
	@Override
	public String toString() {
		return "BankAccount [accountNumber=" + accountNumber + ", createdDate=" + createdDate + ", accountType="
				+ accountType + ", balance=" + balance + ", maxDailyPurchasingLimit=" + maxDailyPurchasingLimit
				+ ", maxDailyWithdrawlLimit=" + maxDailyWithdrawlLimit + ", interestRate=" + interestRate
				+ ", accountStatus=" + accountStatus + ", overDraft=" + overDraft + ", profileId=" + profileId + "]";
	}
	
	
}
