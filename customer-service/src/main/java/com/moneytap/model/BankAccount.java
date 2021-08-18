package com.moneytap.model;

import javax.persistence.*;

@Entity
public class BankAccount {
    @Id
    @SequenceGenerator(name = "account_number_generator", initialValue = 200000)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "account_number_generator")
    private long accountNumber;
    private String IFSCCode;
    private String bankName;
    private double balance;

    @ManyToOne()
    @JoinColumn(name="customerId")
    private Customer customer;

    public BankAccount() {
    }

    public BankAccount(String IFSCCode, String bankName, double balance, Customer customer) {
        this.IFSCCode = IFSCCode;
        this.bankName = bankName;
        this.balance = balance;
        this.customer = customer;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", IFSCCode='" + IFSCCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", balance=" + balance +
                ", customer=" + customer +
                '}';
    }
}


