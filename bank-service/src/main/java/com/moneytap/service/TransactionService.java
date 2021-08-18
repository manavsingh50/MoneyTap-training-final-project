package com.moneytap.service;

import com.moneytap.model.Transaction;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    void addTransaction(Transaction transaction);
    List<Transaction> viewAllTransactions();
    List<Transaction> viewTransactionByType(String transactionType);
    List<Transaction> viewTransactionByTransactionDate(LocalDate date);
}
