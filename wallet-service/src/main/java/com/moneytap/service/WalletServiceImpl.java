package com.moneytap.service;

import com.moneytap.exception.InsufficienBankBalanceException;
import com.moneytap.exception.InsufficientWalletBalanceException;
import com.moneytap.model.*;
import com.moneytap.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
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

    @Override
    public void addWallet(double balance, String customerId, String accountNumber, String walletName) {
        Customer customer = restTemplate.getForObject(customerUrl + customerId, Customer.class);
        BankAccount bankAccount = restTemplate.getForObject(bankUrl+"byId/"+accountNumber, BankAccount.class);
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
    public void addMoney(Long walletId, Double amount) throws InsufficienBankBalanceException {
        Wallet currentUserWallet = walletRepository.findById(walletId).get();
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
        restTemplate.postForObject(bankUrl+"update", currentBankAccount, BankAccount.class);
        Transaction transaction = new Transaction("Credit", new Timestamp(System.currentTimeMillis()),
                amount, "fund received from bank", currentUserWallet);
        transaction.setTransactionId(randomString);
        restTemplate.postForObject(transactionUrl, transaction, Transaction.class);
    }

    @Override
    public void depositMoney(Long walletId, Double amount) throws InsufficientWalletBalanceException {
        Wallet currentUserWallet = walletRepository.findById(walletId).get();
        BankAccount currentBankAccount = currentUserWallet.getBankAccount();
        if(amount> currentUserWallet.getBalance()){
            throw new InsufficientWalletBalanceException("You cannot deposit the requested amount to your" +
                    " bank account because your wallet balance is :"+currentUserWallet.getBalance());
        }
        currentBankAccount.setBalance(currentBankAccount.getBalance()+amount);
        currentUserWallet.setBalance(currentUserWallet.getBalance()-amount);
        walletRepository.save(currentUserWallet);
        restTemplate.postForObject(bankUrl+"update", currentBankAccount, BankAccount.class);
        Transaction transaction = new Transaction("Debit", new Timestamp(System.currentTimeMillis()),
                amount, "fund deposit to bank", currentUserWallet);
        String randomString = getRandomString(20);
        transaction.setTransactionId(randomString);
        restTemplate.postForObject(transactionUrl, transaction, Transaction.class);
    }

    @Override
    public void fundTransfer(Long userWalletId, Long beneficiaryId, Double amount) throws InsufficientWalletBalanceException {
        BeneficiaryDetails beneficiaryDetails = restTemplate.getForObject(beneficiaryUrl+beneficiaryId, BeneficiaryDetails.class);
        Wallet beneficiaryWallet = beneficiaryDetails.getBeneficiaryWallet();
        Wallet userWallet = walletRepository.findById(userWalletId).get();
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
        restTemplate.postForObject(transactionUrl, creditTransaction, Transaction.class);
        restTemplate.postForObject(transactionUrl, debitTransaction, Transaction.class);
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
    public void payBill(String billId) throws InsufficientWalletBalanceException {
        BillPayment billPayment = restTemplate.getForObject(billPaymentUrl+billId, BillPayment.class);
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
        restTemplate.postForObject(transactionUrl, billTransaction, Transaction.class);
        walletRepository.save(wallet);
    }
}
