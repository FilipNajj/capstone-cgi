package com.axlebank.creditCardCompany.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@Entity
public class Company  {

	
	@Id
	private int companyId;
	private String companyName; 
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	private String email;
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate createdDate;
	private long phoneNumber;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CreditCardProduct> productsList;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CreditCardAccount> accountList;
	
	public Company() {}
	
	public Company(int companyId, String companyName, Address address, String email, long phoneNumber,
			List<CreditCardProduct> productsList, List<CreditCardAccount> accountList, String password) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.address = address;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.productsList = productsList;
		this.accountList = accountList;
		this.createdDate = LocalDate.now();
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<CreditCardProduct> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<CreditCardProduct> productsList) {
		this.productsList = productsList;
	}

	public List<CreditCardAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<CreditCardAccount> accountList) {
		this.accountList = accountList;
	}


	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", companyName=" + companyName + ", address=" + address + ", email="
				+ email + ", createdDate=" + createdDate + ", phoneNumber=" + phoneNumber + ", productsList="
				+ productsList + ", accountList=" + accountList + "]";
	}
	
	
	
	
	
	

}
