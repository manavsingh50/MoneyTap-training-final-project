package com.moneytap.service;

import com.moneytap.exception.NotAValidNumberException;
import com.moneytap.model.Customer;
import com.moneytap.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void addCustomer(Customer customer) throws NotAValidNumberException {
        if(customer.getMobileNumber().length()==10){
            customerRepository.save(customer);
        }
        else {
            throw new NotAValidNumberException("Not a valid phone number");
        }
    }

    @Override
    public List<Customer> viewAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        try{
            return customerRepository.findById(customerId).get();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer getCustomerByName(String name) {
        return customerRepository.findByName(name);
    }
}
