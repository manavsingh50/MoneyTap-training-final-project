package com.moneytap.service;

import com.moneytap.model.BeneficiaryDetails;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BeneficiaryDetailsRepository;
import com.moneytap.utility.JWTUtility;
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
    @Autowired
    JWTUtility jwtUtility;
    @Autowired
    CustomerService customerService;

    @Override
    public void addBeneficiary(String beneficiaryWalletId, String token) {
        String actualToken = token.substring(7);
        String userName = jwtUtility.getUsernameFromToken(actualToken);
        Customer customer = customerService.getCustomerByName(userName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Customer> userWalletEntity = new HttpEntity<>(customer,headers);
        ResponseEntity<Wallet> userWalletResponse = restTemplate.exchange(
                walletUrl+"get_wallet_by_user", HttpMethod.POST, userWalletEntity, Wallet.class);
        Wallet userWallet = userWalletResponse.getBody();

        HttpEntity beneficiaryWalletEntity = new HttpEntity(headers);
        ResponseEntity<Wallet> beneficiaryWalletResponse = restTemplate.exchange(
                walletUrl+"getWallet/"+beneficiaryWalletId, HttpMethod.GET, beneficiaryWalletEntity, Wallet.class);
        Wallet beneficiaryWallet = beneficiaryWalletResponse.getBody();

        String  beneficiaryNumber = beneficiaryWallet.getCustomer().getMobileNumber();
        String beneficiaryName = beneficiaryWallet.getCustomer().getName();

        BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails(customer, beneficiaryWallet, beneficiaryName, beneficiaryNumber, userWallet);
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
