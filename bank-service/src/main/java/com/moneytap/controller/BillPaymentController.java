package com.moneytap.controller;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BillPayment;
import com.moneytap.service.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;

@RestController
@RequestMapping("/bill_payment/")
public class BillPaymentController {

    @Autowired
    BillPaymentService billPaymentService;

    @PostMapping("")
    public void addBillPayment(@RequestParam String billType, @RequestParam double amount, @RequestParam Date date,
                               @RequestParam String walletId, @RequestHeader(name="Authorization") String token){
        try {
            billPaymentService.addBill(billType, amount, date, walletId, token);
        } catch (NotAValidAmountException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("{billId}")
    public BillPayment viewBill(@PathVariable String billId){
        return billPaymentService.viewBill(billId);
    }
}
