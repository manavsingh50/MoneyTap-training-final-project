package com.moneytap.controller;

import com.moneytap.exception.UserNotLoggedInException;
import com.moneytap.model.BeneficiaryDetails;
import com.moneytap.model.Customer;
import com.moneytap.model.Token;
import com.moneytap.model.Wallet;
import com.moneytap.service.BeneficiaryDetailsService;
import com.moneytap.service.CustomerService;
import com.moneytap.service.TokenService;
import com.moneytap.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiary/")
public class BeneficiaryDetailsController {
    @Autowired
    BeneficiaryDetailsService beneficiaryDetailsService;
    @Autowired
    JWTUtility jwtUtility;
    @Autowired
    CustomerService customerService;

    @PostMapping("add")
    public void addBeneficiaryDetails(@RequestParam String beneficiaryWalletId, @RequestHeader(name="Authorization") String token){

//        String actualToken = token.substring(7);
//        String userName = jwtUtility.getUsernameFromToken(actualToken);
//        Customer customer = customerService.getCustomerByName(userName);
//        Wallet userWallet = beneficiaryDetailsService.getWalletByUserId(customer, token);
        beneficiaryDetailsService.addBeneficiary(beneficiaryWalletId, token);
    }

    @GetMapping("details/{id}")
    public BeneficiaryDetails viewBeneficiaryDetails(@PathVariable String id){
        Long beneficiaryId = Long.valueOf(id);
        return beneficiaryDetailsService.viewBeneficiaryDetails(beneficiaryId);
    }

    @DeleteMapping("remove/{id}")
    public void removeBeneficiary(@PathVariable String id){
        Long beneficiaryId = Long.valueOf(id);
        beneficiaryDetailsService.removeBeneficiary(beneficiaryId);
    }
}
