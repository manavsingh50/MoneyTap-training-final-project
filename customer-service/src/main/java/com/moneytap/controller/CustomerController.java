package com.moneytap.controller;

import com.moneytap.exception.NotAValidNumberException;
import com.moneytap.exception.UserNotLoggedInException;
import com.moneytap.model.Customer;
import com.moneytap.model.JwtRequest;
import com.moneytap.model.JwtResponse;
import com.moneytap.model.Token;
import com.moneytap.service.CustomerService;
import com.moneytap.service.TokenService;
import com.moneytap.service.UserService;
import com.moneytap.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public void addCustomer(@RequestBody Customer customer)
    {
        try{
            customerService.addCustomer(customer);
        }
        catch (NotAValidNumberException e){
            e.printStackTrace();
        }
    }

    @GetMapping("")
    List<Customer> viewAllCustomers(){
        return customerService.viewAllCustomers();
    }


    @GetMapping("{id}")
    Customer getCustomerById(@PathVariable String id) {
        Long customerId = Long.valueOf(id);
        return customerService.getCustomerById(customerId);
    }

    @PostMapping("authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        Token newToken = new Token(token);
        tokenService.addToken(newToken);

        return  new JwtResponse(token);
    }
    @PostMapping("logout")
    public void logout(@RequestHeader(name="Authorization") String token){
        tokenService.deleteToken(token);
    }

    @GetMapping("getByUserName/{userName}")
    public Customer getByUserName(@PathVariable String userName){
        return customerService.getCustomerByName(userName);
    }
}
