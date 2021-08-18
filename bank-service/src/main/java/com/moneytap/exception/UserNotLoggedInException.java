package com.moneytap.exception;

public class UserNotLoggedInException extends Exception{
    public UserNotLoggedInException(String message){
        super(message);
    }
}
