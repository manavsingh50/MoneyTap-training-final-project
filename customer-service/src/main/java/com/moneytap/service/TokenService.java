package com.moneytap.service;

import com.moneytap.exception.UserNotLoggedInException;
import com.moneytap.model.Token;
import org.springframework.stereotype.Service;


public interface TokenService {
    boolean tokenExists(String token) throws UserNotLoggedInException;
    void addToken(Token token);
    void deleteToken(String token);
}
