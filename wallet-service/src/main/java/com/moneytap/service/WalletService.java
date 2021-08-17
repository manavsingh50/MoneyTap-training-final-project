package com.moneytap.service;

import com.moneytap.exception.InsufficienBankBalanceException;
import com.moneytap.exception.InsufficientWalletBalanceException;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;

public interface WalletService {
    void addWallet(double balance, String customerId, String accountNumber, String walletName);
    Double getBalance(Long walletId);
    void addMoney(Long walletId, Double amount) throws InsufficienBankBalanceException;
    void depositMoney(Long walletId, Double amount) throws InsufficientWalletBalanceException;
    void fundTransfer(Long userWalletId, Long beneficiaryId, Double amount) throws InsufficientWalletBalanceException;
    Wallet getWalletById(Long walletId);
    void payBill(String billId) throws InsufficientWalletBalanceException;
}
