package com.moneytap.service;

import com.moneytap.exception.NotAValidAmountException;
import com.moneytap.model.BillPayment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;

public interface BillPaymentService {
    void addBill(String billType, double amount, Date date, String walletId, String token) throws NotAValidAmountException;
    BillPayment viewBill(String billId);
}
