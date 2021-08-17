package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BillPayment;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void addBill(String billType, double amount, Date date, String walletId) throws NotAValidAmountException {
        if(amount<0.0){
            throw new NotAValidAmountException("Enter a valid amount greater than 0");
        }
        Wallet userWallet = restTemplate.getForObject(walletUrl+"getWallet/"+walletId, Wallet.class);
        BillPayment billPayment = new BillPayment(billType, amount, date, userWallet);
        billPaymentRepository.save(billPayment);
    }

    @Override
    public BillPayment viewBill(String billId) {
        return billPaymentRepository.findById(billId).get();
    }
}
