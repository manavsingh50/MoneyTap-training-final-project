package com.moneytap.service;

import com.moneytap.model.Transaction;
import com.moneytap.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    @Override
    public List<Transaction> viewTransactionByType(String transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<Transaction> viewTransactionByTransactionDate(LocalDate date) {
        Timestamp timeStampBegin = Timestamp.valueOf(date.atStartOfDay());
        Timestamp timeStampEnd = Timestamp.valueOf(date.plusDays(1).atStartOfDay());
        return transactionRepository.findByTransactionDateBetween(timeStampBegin, timeStampEnd);
    }
}
