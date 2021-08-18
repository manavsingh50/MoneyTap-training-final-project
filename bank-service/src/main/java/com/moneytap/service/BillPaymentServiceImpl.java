package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BillPayment;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BillPaymentRepository;
import com.moneytap.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Timestamp;

@Service
public class BillPaymentServiceImpl implements BillPaymentService{
    private static String walletUrl = "http://wallet-service/wallet/";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BillPaymentRepository billPaymentRepository;

    @Autowired
    JWTUtility jwtUtility;

    @Override
    public void addBill(String billType, double amount, Date date, String walletId, String token) throws NotAValidAmountException {
        if(amount<0.0){
            throw new NotAValidAmountException("Enter a valid amount greater than 0");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Wallet> response = restTemplate.exchange(
                walletUrl+"getWallet/"+walletId, HttpMethod.GET, entity, Wallet.class);
        Wallet userWallet = response.getBody();

        BillPayment billPayment = new BillPayment(billType, amount, date, userWallet);
        billPaymentRepository.save(billPayment);
    }

    @Override
    public BillPayment viewBill(String billId) {
        return billPaymentRepository.findById(billId).get();
    }
}
