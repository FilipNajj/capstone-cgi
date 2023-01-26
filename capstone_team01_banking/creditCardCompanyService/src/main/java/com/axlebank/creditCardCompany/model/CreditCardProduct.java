package com.axlebank.creditCardCompany.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;


@Entity
public class CreditCardProduct  {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ccProductId;
	private String name;
	@Enumerated(EnumType.STRING)
	private Type type;
	private String description;
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startingDate;
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate endingDate;
	private int monthsToExpire;
	private double cashAdvanceInterestRate;
	private double purchaseInterestRate;
	private double averageRating;

	
	public CreditCardProduct() {}
	
	public enum Type{Visa, MasterCard, Amex}
	
	

	public CreditCardProduct(int ccProductId, String name, Type type, String description, LocalDate startingDate,
			LocalDate endingDate, int monthsToExpire, double cashAdvanceInterestRate, double purchaseInterestRate,
			double averageRating) {
		super();
		this.ccProductId = ccProductId;
		this.name = name;
		this.type = type;
		this.description = description;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.monthsToExpire = monthsToExpire;
		this.cashAdvanceInterestRate = cashAdvanceInterestRate;
		this.purchaseInterestRate = purchaseInterestRate;
		this.averageRating = averageRating;
	}

	public int getCcProductId() {
		return ccProductId;
	}

	public void setCcProductId(int ccProductId) {
		this.ccProductId = ccProductId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public LocalDate getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}

	public int getMonthsToExpire() {
		return monthsToExpire;
	}

	public void setMonthsToExpire(int monthsToExpire) {
		this.monthsToExpire = monthsToExpire;
	}

	public double getCashAdvanceInterestRate() {
		return cashAdvanceInterestRate;
	}

	public void setCashAdvanceInterestRate(double cashAdvanceInterestRate) {
		this.cashAdvanceInterestRate = cashAdvanceInterestRate;
	}

	public double getPurchaseInterestRate() {
		return purchaseInterestRate;
	}

	public void setPurchaseInterestRate(double purchaseInterestRate) {
		this.purchaseInterestRate = purchaseInterestRate;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	@Override
	public String toString() {
		return "CreditCardProduct [ccProductId=" + ccProductId + ", name=" + name + ", type=" + type + ", description="
				+ description + ", startingDate=" + startingDate + ", endingDate=" + endingDate + ", monthsToExpire="
				+ monthsToExpire + ", cashAdvanceInterestRate=" + cashAdvanceInterestRate + ", purchaseInterestRate="
				+ purchaseInterestRate + ", averageRating=" + averageRating + "]";
	}


	

}
