package com.axlebank.adminservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @JsonIgnore
    private int adminId;
    private String streetName;
    private int streetNumber;
    private String city;
    private  String postalCode;
    @OneToOne
    @MapsId
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;


    public Address() {
    }

    public Address(int adminId, String streetName, int streetNumber, String city, String postalCode, Admin admin) {
        this.adminId = adminId;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.admin = admin;
    }

    public int getAdminId(){
        return this.adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.adminId = admin.getAdminId();
    }
}
