package com.moneytap.controller;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BankAccount;
import com.moneytap.model.Customer;
import com.moneytap.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank_account/")
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("")
    public List<BankAccount> getAllAccounts(){
        return bankAccountService.getAllBankAccounts();
    }

    @PostMapping("")
    public void addBankAccount(@RequestParam String IFSCCode, @RequestParam String bankName,
                               @RequestParam double balance,
                               @RequestHeader(name="Authorization") String token)
    {
        try {
            bankAccountService.addBankAccount(IFSCCode, bankName, balance, token);
        } catch (NotAValidAmountException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("{accNumber}")
    public void removeAccount(@PathVariable String accNumber){
        Long accountNumber = Long.valueOf(accNumber);
        bankAccountService.removeAccount(accountNumber);
    }

    @PostMapping("update")
    public void updateAccount(@RequestBody BankAccount bankAccount){
        bankAccountService.updateAccount(bankAccount);
    }

    @GetMapping("byId/{accNumber}")
    public BankAccount getAccountById(@PathVariable String accNumber){
        Long accountNumber = Long.valueOf(accNumber);
        return bankAccountService.getAccountById(accountNumber);
    }

    @PostMapping("getByUserName")
    public BankAccount getAccountByUserName(@RequestBody Customer customer){
        return bankAccountService.getAccountByCustomer(customer);
    }
}
