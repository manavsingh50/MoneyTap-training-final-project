package com.moneytap.service;

import com.moneytap.exception.InsufficienBankBalanceException;
import com.moneytap.exception.InsufficientWalletBalanceException;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;

public interface WalletService {
    void addWallet(double balance, String token, String walletName);
    Double getBalance(Long walletId);
    void addMoney(String token, Double amount) throws InsufficienBankBalanceException;
    void depositMoney(String token, Double amount) throws InsufficientWalletBalanceException;
    void fundTransfer(String token, Long beneficiaryId, Double amount) throws InsufficientWalletBalanceException;
    Wallet getWalletById(Long walletId);
    void payBill(String billId, String token) throws InsufficientWalletBalanceException;
    Wallet getWalletByCustomer(Customer customer);
}
