package com.axlebank.creditCardCompany.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CreditCardAccount {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int creditAccountNumber;
	private Date expirationDate;
	private double creditLimit;
	private double usedCredit;
	private double rating;
	
	@ManyToOne
	private CreditCardProduct creditCardProduct;
	
	public CreditCardAccount() {}

	public int getCreditAccountNumber() {
		return creditAccountNumber;
	}

	public void setCreditAccountNumber(int creditAccountNumber) {
		this.creditAccountNumber = creditAccountNumber;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public double getUsedCredit() {
		return usedCredit;
	}

	public void setUsedCredit(double usedCredit) {
		this.usedCredit = usedCredit;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public CreditCardProduct getCreditCardProduct() {
		return creditCardProduct;
	}

	public void setCreditCardProduct(CreditCardProduct creditCardProduct) {
		this.creditCardProduct = creditCardProduct;
	}
	
	

}
