package com.axlebank.adminservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Admin {
    @Id
    private int adminId;
    private String firstName;
    private String lastName;

    private String email;
    private LocalDate createDate;
    private String phoneNumber;
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address  address;

    public Admin() {
        this.createDate = LocalDate.now();
    }

    public Admin(int adminId, String firstName, String lastName, Address address, String email, String phoneNumber) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.createDate = LocalDate.now();
        this.phoneNumber = phoneNumber;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
