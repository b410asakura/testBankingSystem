package com.example.banking.service;

import com.example.banking.dto.request.AccountRequest;
import com.example.banking.dto.response.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse deposit(Long id, BigDecimal amount);

    AccountResponse withDraw(Long id, BigDecimal amount);

    List<AccountResponse> getAccountsByUser(String userEmail);

    ResponseEntity<Void> deleteById(Long id);

    BigDecimal findTotalBalance();





}
