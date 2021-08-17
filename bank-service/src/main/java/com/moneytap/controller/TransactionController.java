package com.moneytap.controller;


import com.moneytap.model.Transaction;
import com.moneytap.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transaction/")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("")
    public void addTransaction(@RequestBody Transaction transaction){
        transactionService.addTransaction(transaction);
    }

    @GetMapping("")
    public List<Transaction> viewAllTransactions(){
        return transactionService.viewAllTransactions();
    }

    @GetMapping("get_by_type/{type}")
    public List<Transaction> viewTransactionsByType(@PathVariable String type){
        return transactionService.viewTransactionByType(type);
    }

    @GetMapping("get_by_date/{strDate}")
    public List<Transaction> viewTransactionsByDate(@PathVariable String strDate){
        LocalDate date = LocalDate.parse(strDate);
        return transactionService.viewTransactionByTransactionDate(date);
    }
}
