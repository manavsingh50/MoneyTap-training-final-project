package com.moneytap.controller;

import com.moneytap.exception.InsufficienBankBalanceException;
import com.moneytap.exception.InsufficientWalletBalanceException;
import com.moneytap.model.Customer;
import com.moneytap.model.Wallet;
import com.moneytap.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.POST;

@RestController
@RequestMapping("/wallet/")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("")
    public void addWallet(@RequestParam double balance, @RequestParam String customerId, @RequestParam String accountNumber, @RequestParam String walletName)
    {
        walletService.addWallet(balance, customerId, accountNumber, walletName);
    }

    @GetMapping("getWallet/{id}")
    public Wallet getWalletById(@PathVariable String id){
        Long walletId = Long.valueOf(id);
        return walletService.getWalletById(walletId);
    }

    @GetMapping("getBalance/{id}")
    public double getBalance(@PathVariable String id){
        Long walletId = Long.valueOf(id);
        return walletService.getBalance(walletId);
    }

    @PostMapping("add/{strWalletId}/{strAmount}")
    public void addMoney(@PathVariable String strWalletId, @PathVariable String strAmount){
        Long walletId = Long.valueOf(strWalletId);
        Double amount = Double.valueOf(strAmount);
        try {
            walletService.addMoney(walletId,amount);
        } catch (InsufficienBankBalanceException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("deposit/{strWalletId}/{strAmount}")
    public void depositMoney(@PathVariable String strWalletId, @PathVariable String strAmount){
        Long walletId = Long.valueOf(strWalletId);
        Double amount = Double.valueOf(strAmount);

        try {
            walletService.depositMoney(walletId,amount);
        } catch (InsufficientWalletBalanceException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("transfer")
    public void fundTransfer(@RequestParam String userWallet, @RequestParam String strBeneficiaryId, @RequestParam Double amount){
        Long userWalletId = Long.valueOf(userWallet);
        Long beneficiaryId = Long.valueOf(strBeneficiaryId);
        Double fundAmount = Double.valueOf(amount);
        try {
            walletService.fundTransfer(userWalletId, beneficiaryId, fundAmount);
        } catch (InsufficientWalletBalanceException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("bill_payment")
    public void payBill(@RequestParam String billId){
        try {
            walletService.payBill(billId);
        } catch (InsufficientWalletBalanceException e) {
            e.printStackTrace();
        }
    }
}
