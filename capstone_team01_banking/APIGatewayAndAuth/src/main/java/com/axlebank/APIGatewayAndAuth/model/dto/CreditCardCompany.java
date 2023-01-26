package com.axlebank.APIGatewayAndAuth.model.dto;

public class CreditCardCompany {

    private int companyId;

    private String companyName;

    private String email;

    private Address address;

    public CreditCardCompany(int companyId, String companyName, String email, Address address) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.email = email;
        this.address = address;
    }

    public CreditCardCompany() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
