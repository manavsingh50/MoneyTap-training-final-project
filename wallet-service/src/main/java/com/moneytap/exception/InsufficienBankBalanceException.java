package com.moneytap.exception;

public class InsufficienBankBalanceException extends Exception{
    public InsufficienBankBalanceException(String message){
        super(message);
    }
}
