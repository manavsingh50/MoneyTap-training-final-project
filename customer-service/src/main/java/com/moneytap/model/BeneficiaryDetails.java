package com.moneytap.model;

import javax.persistence.*;

@Entity
public class BeneficiaryDetails {
    @Id
    @SequenceGenerator(name = "beneficiary_id_generator", initialValue = 3000)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "beneficiary_id_generator")
    private long beneficiaryId;

    @ManyToOne
    @JoinColumn(name="customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="beneficiaryWalletId")
    private Wallet beneficiaryWallet;

    private String Name;
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name="customerWalletId")
    private Wallet userWallet;

    public BeneficiaryDetails() {
    }

    public BeneficiaryDetails(Customer customer, Wallet beneficiaryWallet, String name, String mobileNumber, Wallet userWallet) {
        this.customer = customer;
        this.beneficiaryWallet = beneficiaryWallet;
        this.Name = name;
        this.mobileNumber = mobileNumber;
        this.userWallet = userWallet;
    }

    public long getId() {
        return beneficiaryId;
    }

    public void setId(long id) {
        this.beneficiaryId = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Wallet getBeneficiaryWallet() {
        return beneficiaryWallet;
    }

    public void setBeneficiaryWallet(Wallet beneficiaryWallet) {
        this.beneficiaryWallet = beneficiaryWallet;
    }

    public Wallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(Wallet userWallet) {
        this.userWallet = userWallet;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "BeneficiaryDetails{" +
                "id=" + beneficiaryId +
                ", customer=" + customer +
                ", beneficiaryWallet=" + beneficiaryWallet +
                ", Name='" + Name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", userWallet=" + userWallet +
                '}';
    }
}
