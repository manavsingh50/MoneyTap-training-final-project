package com.moneytap.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {
    @Id
    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
