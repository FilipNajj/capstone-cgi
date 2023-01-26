package com.axlebank.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


//@Entity
//@Table(name="clients")
public class Client {
/*
 * - profileId: int (Primary Key)
	- firstName: String (text, max_50, required)
	- lastName: String (text, max_50, required)
	- Address address
	- email : String (text, max_50, required)
	- createdDate: Date;
	- accounts: List<BankAccount>
	- cardsList: List<Card>
	- institutionsList: List<Institution>
	- recipientList: List<recipient>
	- profileStatus: enum[ACTIVE, INACTIVE]
	(- creditScore: int)
	- phoneNumber : long
	- password : string

 */
	
//	@Id
	private Integer profileId;
	private String firstName;
	private String lastName;
	private Address address;
	private String email;
	private LocalDate createdDate = LocalDate.now();
	private List<Account> accounts;
	private List<CreditCard> cardsList;
	private List<Institution> institutionList;
	private List<Recipient> recipientList;
	private ProfileStatus profileStatus;
	private long phoneNumber;
	private String password;

	public Client () {}
	
	public Client(Integer profileId, String firstName, String lastName, Address address, String email,
			List<Account> accounts, List<CreditCard> cardsList,
			List<Institution> institutionList, List<Recipient> recipientList, ProfileStatus profileStatus,
			long phoneNumber, String password) {
		this.profileId = profileId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = email;
		this.accounts = accounts;
		this.cardsList = cardsList;
		this.institutionList = institutionList;
		this.recipientList = recipientList;
		this.profileStatus = profileStatus;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}
	
	public Integer getProfileId() {
		return profileId;
	}
	
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
		this.createdDate = LocalDate.now();
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public List<CreditCard> getCardsList() {
		return cardsList;
	}
	
	public void setCardsList(List<CreditCard> cardsList) {
		this.cardsList = cardsList;
	}
	
	public List<Institution> getInstitutionList() {
		return institutionList;
	}
	
	public void setInstitutionList(List<Institution> institutionList) {
		this.institutionList = institutionList;
	}
	
	public List<Recipient> getRecipientList() {
		return recipientList;
	}
	
	public void setRecipientList(List<Recipient> recipientList) {
		this.recipientList = recipientList;
	}
	
	public ProfileStatus getProfileStatus() {
		return profileStatus;
	}
	
	public void setProfileStatus(ProfileStatus profileStatus) {
		this.profileStatus = profileStatus;
	}
		
	public long getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Client [profileId=" + profileId + ", firstName=" + firstName + ", lastName=" + lastName + ", address="
				+ address + ", email=" + email + ", createdDate=" + createdDate + ", accounts=" + accounts
				+ ", cardsList=" + cardsList + ", institutionList=" + institutionList + ", recipientList="
				+ recipientList + ", profileStatus=" + profileStatus + ", phoneNumber="
				+ phoneNumber + ", password=" + password + "]";
	}

	
	
}
