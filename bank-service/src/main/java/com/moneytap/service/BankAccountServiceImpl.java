package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BankAccount;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BankAccountServiceImpl implements BankAccountService{

    private static String customerUrl = "http://customer-service/customer/";
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void addBankAccount(String IFSCCode, String bankName, double balance, String customerId, String token) throws NotAValidAmountException {
        if(balance<0.0){
            throw new NotAValidAmountException("Not a valid balance, enter amount greater than 0");
        }
        else {
            Customer customer = restTemplate.getForObject(customerUrl + customerId, Customer.class);
            System.out.println("hello");
            System.out.println(customer);
            BankAccount bankAccount = new BankAccount(IFSCCode, bankName, balance, customer);
            bankAccountRepository.save(bankAccount);
        }
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return (List<BankAccount>) bankAccountRepository.findAll();
    }

    @Override
    public void removeAccount(Long accountNumber) {
        bankAccountRepository.deleteById(accountNumber);
    }

    @Override
    public void updateAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount getAccountById(Long accountNumber){
        try {
            return bankAccountRepository.findById(accountNumber).get();
        }
        catch ( NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }
}
