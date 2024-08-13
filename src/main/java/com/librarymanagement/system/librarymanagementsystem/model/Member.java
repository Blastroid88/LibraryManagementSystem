package com.librarymanagement.system.librarymanagementsystem.model;

import java.time.LocalDate;

public class Member {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipDate;

    public Member() {
    }

    public Member(int id, String name, String email, String phone, String address, LocalDate membershipDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipDate = membershipDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    @Override
    // Display the member's name in the ComboBox
    public String toString() {
        return name;
    }
}
