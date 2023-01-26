package com.axlebank.model;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

//@Entity
public class Institution {
	
	
//	@Id
	private int institutionId;
	private String institutionName;
	private String accountNumber;
	
	public Institution() {}

	public Institution(int institutionId, String institutionName, String accountNumber) {
		this.institutionId = institutionId;
		this.institutionName = institutionName;
		this.accountNumber = accountNumber;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Institution [institutionId=" + institutionId + ", institutionName=" + institutionName
				+ ", accountNumber=" + accountNumber + "]";
	}
	
}
