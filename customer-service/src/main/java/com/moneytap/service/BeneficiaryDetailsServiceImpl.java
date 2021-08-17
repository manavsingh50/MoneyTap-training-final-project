package com.moneytap.service;

import com.moneytap.model.BeneficiaryDetails;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BeneficiaryDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Service
public class BeneficiaryDetailsServiceImpl implements BeneficiaryDetailsService{
    private static String walletUrl = "http://wallet-service/wallet/";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BeneficiaryDetailsRepository beneficiaryDetailsRepository;

    @Override
    public void addBeneficiary(String userWalletId, String beneficiaryWalletId) {
        Wallet userWallet = restTemplate.getForObject(walletUrl+"getWallet/"+userWalletId, Wallet.class);
        Wallet beneficiaryWallet = restTemplate.getForObject(walletUrl+"getWallet/"+beneficiaryWalletId, Wallet.class);
        Customer user = userWallet.getCustomer();
        String  beneficiaryNumber = beneficiaryWallet.getCustomer().getMobileNumber();
        String beneficiaryName = beneficiaryWallet.getCustomer().getName();
        BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails(user, beneficiaryWallet, beneficiaryName, beneficiaryNumber, userWallet);
        beneficiaryDetailsRepository.save(beneficiaryDetails);
    }

    @Override
    public BeneficiaryDetails viewBeneficiaryDetails(Long beneficiaryId) {
        try {
            return beneficiaryDetailsRepository.findById(beneficiaryId).get();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeBeneficiary(Long beneficiaryId) {
        beneficiaryDetailsRepository.deleteById(beneficiaryId);
    }

    @Override
    public Wallet getWalletByUserId(Customer customer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Customer> request = new HttpEntity<>(customer,headers);
        ResponseEntity<Wallet> response = restTemplate.exchange(walletUrl+"getByUserId", HttpMethod.POST, request, Wallet.class);
        return response.getBody();
    }
}
