package com.moneytap.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transactionId;
    private String transactionType;
    private Timestamp transactionDate;
    private double amount;
    private String description;

    @ManyToOne
    @JoinColumn(name="walletId")
    private Wallet wallet;

    public Transaction() {
    }

    public Transaction(String transactionType, Timestamp transactionDate, double amount, String description, Wallet wallet) {
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionDate=" + transactionDate +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
