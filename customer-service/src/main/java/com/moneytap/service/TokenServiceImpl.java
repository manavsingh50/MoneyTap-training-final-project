package com.moneytap.service;

import com.moneytap.exception.UserNotLoggedInException;
import com.moneytap.model.Token;
import com.moneytap.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    TokenRepository tokenRepository;

    @Override
    public boolean tokenExists(String token) throws UserNotLoggedInException {
        if(tokenRepository.existsById(token)){
            return true;
        }
        else {
            throw new UserNotLoggedInException("Please log in again");
        }
    }

    @Override
    public void addToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void deleteToken(String token) {
        String actualToken = token.substring(7);
        tokenRepository.deleteById(actualToken);
    }
}
