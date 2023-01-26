package com.axlebank.model;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GenericGenerator(name = "accid-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "user_sequence"),
			@Parameter(name = "initial_value", value = "1000"), 
			@Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "accid-sequence-generator",strategy = GenerationType.IDENTITY)
	private int accountNumber;
	private double balance;
	private double maxDailyPurchasingLimit;
	private double maxDailyWithdrawlLimit;
	private double interestRate;
	private double overDraft;
	private LocalDate creationDate = LocalDate.now();

	private int profileId;

	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	public Account() {
	}
		
	
	public Account(int accountNumber, double balance, double maxDailyPurchasingLimit, double maxDailyWithdrawlLimit,
			double interestRate, double overDraft, LocalDate creationDate, int profileId, AccountType accountType,
			AccountStatus accountStatus) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.maxDailyPurchasingLimit = maxDailyPurchasingLimit;
		this.maxDailyWithdrawlLimit = maxDailyWithdrawlLimit;
		this.interestRate = interestRate;
		this.overDraft = overDraft;
		this.creationDate = creationDate;
		this.profileId = profileId;
		this.accountType = accountType;
		this.accountStatus = accountStatus;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
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

	public double getOverDraft() {
		return overDraft;
	}

	public void setOverDraft(double overDraft) {
		this.overDraft = overDraft;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate() {
		this.creationDate = LocalDate.now();
	}

	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountNumber=" + accountNumber +
				", balance=" + balance +
				", maxDailyPurchasingLimit=" + maxDailyPurchasingLimit +
				", maxDailyWithdrawlLimit=" + maxDailyWithdrawlLimit +
				", interestRate=" + interestRate +
				", overDraft=" + overDraft +
				", creationDate=" + creationDate +
				", profileId=" + profileId +
				", accountType=" + accountType +
				", accountStatus=" + accountStatus +
				'}';
	}
}
