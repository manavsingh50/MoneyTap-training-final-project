package com.moneytap.exception;

public class NotAValidAmountException extends Exception{
    public NotAValidAmountException(String message){
        super(message);
    }
}
