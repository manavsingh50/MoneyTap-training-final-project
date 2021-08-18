package com.moneytap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_id_generator", initialValue = 1000)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "customer_id_generator")
    private long customerId;

    private String name;
    private String mobileNumber;
    private String password;

    public Customer() {
    }

    public Customer(String name, String mobileNumber, String password) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
