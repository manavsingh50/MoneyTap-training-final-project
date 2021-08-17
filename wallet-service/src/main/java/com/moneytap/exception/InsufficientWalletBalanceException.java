package com.moneytap.exception;

public class InsufficientWalletBalanceException extends Exception{
    public InsufficientWalletBalanceException(String message){
        super(message);
    }
}
