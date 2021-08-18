package com.moneytap.service;

import com.moneytap.exception.UserNotLoggedInException;
import com.moneytap.model.Token;


public interface TokenService {
    boolean tokenExists(String token) throws UserNotLoggedInException;
    void addToken(Token token);
}
