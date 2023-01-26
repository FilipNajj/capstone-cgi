package com.axlebank.creditCardCompany.model;

//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.Table;

@Entity
public class Address  {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int addressId;
	private String streetName;
	private int streetNumber;
	private String city;
	private String postalCode;

	public Address() {
	}

	public Address( String streetName, int streetNumber, String city, String postalCode) {
		super();
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postalCode = postalCode;
	}

	public int getAddreessId() {
		return addressId;
	}

	public void setAddreessId(int addreessId) {
		this.addressId = addreessId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public String toString() {
		return "Address [addreessId=" + addressId + ", streetName=" + streetName + ", streetNumber=" + streetNumber
				+ ", city=" + city + ", postalCode=" + postalCode + "]";
	}

}
