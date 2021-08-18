package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BankAccount;
import com.moneytap.model.Customer;

import java.util.List;

public interface BankAccountService {
    void addBankAccount(String IFSCCode,String bankName,double balance, String token) throws NotAValidAmountException;
    List<BankAccount> getAllBankAccounts();
    void removeAccount(Long accountNumber);
    void updateAccount(BankAccount bankAccount);
    BankAccount getAccountById(Long accountNumber);
    BankAccount getAccountByCustomer(Customer customer);

}
