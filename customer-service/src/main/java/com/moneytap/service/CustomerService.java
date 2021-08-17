package com.moneytap.service;

import com.moneytap.exception.NotAValidNumberException;
import com.moneytap.model.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomer(Customer customer) throws NotAValidNumberException;
    List<Customer> viewAllCustomers();
    Customer getCustomerById(Long customerId);
    Customer getCustomerByName(String name);
}
