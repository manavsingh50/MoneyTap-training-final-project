package com.moneytap.repository;

import com.moneytap.model.BankAccount;
import com.moneytap.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    BankAccount findByCustomer(Customer customer);
}
