package com.axlebank.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


@Entity
public class Institution {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer institutionId;
	private String institutionName;
	private long phoneNumber;
	private String category;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDate createdDate = LocalDate.now();
	
	
	
	public Institution() {
		super();
		// TODO Auto-generated constructor stub
	}
	
//	public Institution(Integer institutionId, String institutionName, long phoneNumber) {
//		super();
//		this.institutionId = institutionId;
//		this.institutionName = institutionName;
//		this.phoneNumber = phoneNumber;
//	}

	public Institution( String institutionName, long phoneNumber, String category) {
		super();
		this.institutionName = institutionName;
		this.phoneNumber = phoneNumber;
		this.category = category;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
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
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Institution [institutionId=" + institutionId + ", institutionName=" + institutionName + ", category="
				+ category + ", createdDate=" + createdDate + ", phoneNumber=" + phoneNumber + "]";
	}

	

	
	

}
