package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BankAccount;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.repository.BankAccountRepository;
import com.moneytap.utility.JWTUtility;
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
    JWTUtility jwtUtility;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void addBankAccount(String IFSCCode, String bankName, double balance, String token) throws NotAValidAmountException {
        if(balance<0.0){
            throw new NotAValidAmountException("Not a valid balance, enter amount greater than 0");
        }
        else {
            String actualToken = token.substring(7);
            String userName = jwtUtility.getUsernameFromToken(actualToken);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<Customer> response = restTemplate.exchange(
                    customerUrl+"getByUserName/"+userName, HttpMethod.GET, entity, Customer.class);
            Customer customer = response.getBody();
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

    @Override
    public BankAccount getAccountByCustomer(Customer customer) {
        return bankAccountRepository.findByCustomer(customer);
    }
}
