package com.axlebank.model;

public class Address {
	private String streetName;
	private int streetNumber;
	private String city;
	private String postalCode;
	
	public Address() {}
		
	public Address(String streetName, int streetNumber, String city, String postalCode) {
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postalCode = postalCode;
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
		return "Address [streetName=" + streetName + ", streetNumber=" + streetNumber + ", city=" + city + ", postalCode="
				+ postalCode + "]";
	}
	
	
	
}
