package com.moneytap.repository;

import com.moneytap.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
    List<Transaction> findByTransactionType(String transactionType);
    List<Transaction> findByTransactionDateBetween(Timestamp beginDate, Timestamp endDate);
}
