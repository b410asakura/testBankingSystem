package com.example.banking.service;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.accountDto.AccountRequest;
import com.example.banking.dto.accountDto.AccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse deposit(Long id, BigDecimal amount);

    AccountResponse withDraw(Long id, BigDecimal amount);

    List<AccountResponse> getAccountsByUser(String userEmail);

    SimpleResponse deleteById(Long id);

    BigDecimal findTotalBalance();





}
