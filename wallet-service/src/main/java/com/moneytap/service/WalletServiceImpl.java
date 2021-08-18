package com.moneytap.service;

import com.moneytap.exception.InsufficienBankBalanceException;
import com.moneytap.exception.InsufficientWalletBalanceException;
import com.moneytap.model.*;
import com.moneytap.repository.WalletRepository;
import com.moneytap.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class WalletServiceImpl implements WalletService{
    static String bankUrl = "http://bank-service/bank_account/";
    static String beneficiaryUrl = "http://customer-service/beneficiary/details/";
    static String transactionUrl = "http://bank-service/transaction/";
    private static String customerUrl = "http://customer-service/customer/";
    private static String billPaymentUrl = "http://bank-service/bill_payment/";
    static String getRandomString(int n)
    {
        int lowerLimit = 97;
        int upperLimit = 122;

        Random random = new Random();
        StringBuffer r = new StringBuffer(n);
        for (int i = 0; i < n; i++) {
            int nextRandomChar = lowerLimit
                    + (int)(random.nextFloat()
                    * (upperLimit - lowerLimit + 1));
            r.append((char)nextRandomChar);
        }
        return r.toString();
    }
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JWTUtility jwtUtility;

    @Override
    public void addWallet(double balance, String token, String walletName) {
        String actualToken = token.substring(7);
        String userName = jwtUtility.getUsernameFromToken(actualToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Customer> customerResponse = restTemplate.exchange(
                customerUrl+"getByUserName/"+userName, HttpMethod.GET, entity, Customer.class);
        Customer customer = customerResponse.getBody();

        HttpEntity<Customer> accountEntity = new HttpEntity<>(customer,headers);
        ResponseEntity<BankAccount> bankAccountResponse = restTemplate.exchange(
                bankUrl+"getByUserName", HttpMethod.POST, accountEntity, BankAccount.class);
        BankAccount bankAccount = bankAccountResponse.getBody();
        Wallet wallet = new Wallet(balance, customer, bankAccount, walletName);
        walletRepository.save(wallet);
    }

    @Override
    public Double getBalance(Long walletId) {
        try {
            return walletRepository.findById(walletId).get().getBalance();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addMoney(String token, Double amount) throws InsufficienBankBalanceException {
        String actualToken = token.substring(7);
        String userName = jwtUtility.getUsernameFromToken(actualToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                customerUrl+"getByUserName/"+userName, HttpMethod.GET, entity, Customer.class);
        Customer customer = response.getBody();

        Wallet currentUserWallet = walletRepository.findByCustomer(customer);
        BankAccount currentBankAccount = currentUserWallet.getBankAccount();

        double currentBankBalance = currentBankAccount.getBalance();
        if(amount>currentBankBalance){
            throw new InsufficienBankBalanceException("You cannot add the requested amount" +
                    " to your wallet because your account balance is :"+currentBankBalance);
        }

        currentBankAccount.setBalance(currentBankBalance-amount);
        currentUserWallet.setBalance(currentUserWallet.getBalance()+amount);
        String randomString = getRandomString(20);
        walletRepository.save(currentUserWallet);

        HttpEntity<BankAccount> accountUpdateRequest = new HttpEntity<>(currentBankAccount, headers);
        restTemplate.postForObject(bankUrl+"update", accountUpdateRequest, Void.class);

        Transaction transaction = new Transaction("Credit", new Timestamp(System.currentTimeMillis()),
                amount, "fund received from bank", currentUserWallet);
        transaction.setTransactionId(randomString);
        HttpEntity<Transaction> transactionCreationRequest = new HttpEntity<>(transaction, headers);
        restTemplate.postForObject(transactionUrl, transactionCreationRequest, Void.class);
    }

    @Override
    public void depositMoney(String token, Double amount) throws InsufficientWalletBalanceException {
        String actualToken = token.substring(7);
        String userName = jwtUtility.getUsernameFromToken(actualToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                customerUrl+"getByUserName/"+userName, HttpMethod.GET, entity, Customer.class);
        Customer customer = response.getBody();

        Wallet currentUserWallet = walletRepository.findByCustomer(customer);
        BankAccount currentBankAccount = currentUserWallet.getBankAccount();

        if(amount> currentUserWallet.getBalance()){
            throw new InsufficientWalletBalanceException("You cannot deposit the requested amount to your" +
                    " bank account because your wallet balance is :"+currentUserWallet.getBalance());
        }

        currentBankAccount.setBalance(currentBankAccount.getBalance()+amount);
        currentUserWallet.setBalance(currentUserWallet.getBalance()-amount);

        walletRepository.save(currentUserWallet);

        HttpEntity<BankAccount> accountUpdateRequest = new HttpEntity<>(currentBankAccount, headers);
        restTemplate.postForObject(bankUrl+"update", accountUpdateRequest, Void.class);

        Transaction transaction = new Transaction("Debit", new Timestamp(System.currentTimeMillis()),
                amount, "fund deposit to bank", currentUserWallet);
        String randomString = getRandomString(20);
        transaction.setTransactionId(randomString);
        HttpEntity<Transaction> transactionCreationRequest = new HttpEntity<>(transaction, headers);
        restTemplate.postForObject(transactionUrl, transactionCreationRequest, Void.class);
    }

    @Override
    public void fundTransfer(String token, Long beneficiaryId, Double amount) throws InsufficientWalletBalanceException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<BeneficiaryDetails> response = restTemplate.exchange(
                beneficiaryUrl+beneficiaryId, HttpMethod.GET, entity, BeneficiaryDetails.class);
        BeneficiaryDetails beneficiaryDetails = response.getBody();

        Wallet beneficiaryWallet = beneficiaryDetails.getBeneficiaryWallet();
        Wallet userWallet = beneficiaryDetails.getUserWallet();
        if(amount>userWallet.getBalance()){
            throw new InsufficientWalletBalanceException("You cannot deposit the requested amount to your" +
                    " bank account because your wallet balance is :"+userWallet.getBalance());
        }
        userWallet.setBalance(userWallet.getBalance()-amount);
        beneficiaryWallet.setBalance(beneficiaryWallet.getBalance()+amount);
        String randomString = getRandomString(20);
        Transaction creditTransaction = new Transaction("Credit", new Timestamp(System.currentTimeMillis()), amount, "fund received from a friend", beneficiaryWallet);
        Transaction debitTransaction = new Transaction("Debit", new Timestamp(System.currentTimeMillis()), amount, "fund transferred to a friend", userWallet);
        creditTransaction.setTransactionId(randomString);
        debitTransaction.setTransactionId(randomString);
        HttpEntity<Transaction> creditTransactionCreationRequest = new HttpEntity<>(creditTransaction, headers);
        HttpEntity<Transaction> debitTransactionCreationRequest = new HttpEntity<>(debitTransaction, headers);

        restTemplate.postForObject(transactionUrl, creditTransactionCreationRequest, Void.class);
        restTemplate.postForObject(transactionUrl, debitTransactionCreationRequest, Void.class);
        walletRepository.save(userWallet);
        walletRepository.save(beneficiaryWallet);
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        try {
            return walletRepository.findById(walletId).get();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void payBill(String billId, String token) throws InsufficientWalletBalanceException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<BillPayment> response = restTemplate.exchange(
                billPaymentUrl+billId, HttpMethod.GET, entity, BillPayment.class);
        BillPayment billPayment = response.getBody();
        Wallet wallet = billPayment.getWallet();
        if(billPayment.getAmount()>wallet.getBalance()){
            throw new InsufficientWalletBalanceException("You cannot deposit the requested amount to your" +
                    " bank account because your wallet balance is :"+wallet.getBalance());
        }
        wallet.setBalance(wallet.getBalance()-billPayment.getAmount());
        String randomString = getRandomString(20);
        Transaction billTransaction = new Transaction("Debit", new Timestamp(System.currentTimeMillis()),
                billPayment.getAmount(), "fund transfer for bill payment", wallet);
        billTransaction.setTransactionId(randomString);
        HttpEntity<Transaction> billTransactionCreationRequest = new HttpEntity<>(billTransaction, headers);
        restTemplate.postForObject(transactionUrl, billTransactionCreationRequest, Void.class);
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletByCustomer(Customer customer) {
        return walletRepository.findByCustomer(customer);
    }
}
