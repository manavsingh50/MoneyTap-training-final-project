package com.moneytap.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class BillPayment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String billId;

    private String billType;
    private double amount;
    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name="walletId")
    private Wallet wallet;

    public BillPayment() {
    }

    public BillPayment(String billType, double amount, Date paymentDate, Wallet wallet) {
        this.billType = billType;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.wallet = wallet;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "BillPayment{" +
                "billId=" + billId +
                ", billType='" + billType + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", wallet=" + wallet +
                '}';
    }
}
