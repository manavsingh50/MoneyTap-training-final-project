package com.moneytap.repository;

import com.moneytap.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
