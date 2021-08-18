package com.moneytap.model;

import javax.persistence.*;

@Entity
public class Wallet {
    @Id
    @SequenceGenerator(name = "wallet_id_generator", initialValue = 5000)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "wallet_id_generator")
    private long walletId;
    private double balance;

    @ManyToOne()
    @JoinColumn(name="customerId")
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name="accountNumber")
    private BankAccount bankAccount;
    private String walletName;

    public Wallet() {
    }

    public Wallet(double balance, Customer customer, BankAccount bankAccount, String walletName) {
        this.balance = balance;
        this.customer = customer;
        this.bankAccount = bankAccount;
        this.walletName = walletName;
    }

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(long walletId) {
        this.walletId = walletId;
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", balance=" + balance +
                ", customer=" + customer +
                ", bankAccount=" + bankAccount +
                ", walletName='" + walletName + '\'' +
                '}';
    }
}
