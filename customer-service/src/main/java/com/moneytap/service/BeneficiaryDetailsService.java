package com.moneytap.service;

import com.moneytap.model.BeneficiaryDetails;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;

import java.util.List;

public interface BeneficiaryDetailsService {
    void addBeneficiary(String userWalletId, String beneficiaryWalletId);
    BeneficiaryDetails viewBeneficiaryDetails(Long beneficiaryId);
    void removeBeneficiary(Long beneficiaryId);
    Wallet getWalletByUserId(Customer customer, String token);
//    List<BeneficiaryDetails> viewAllBeneficiaries(Wallet wallet);
}
