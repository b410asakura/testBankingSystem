package com.example.banking.service;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.accountDto.AccountRequest;
import com.example.banking.dto.accountDto.AccountResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import com.example.banking.exception.AccountNotFoundException;
import com.example.banking.exception.UserNotFoundException;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccountShouldReturnAccount() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        AccountRequest accountRequest = new AccountRequest("12312", "test@gmail.com");
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("12312");
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse accountResponse = accountService.createAccount(accountRequest);

        assertEquals("12312", accountResponse.accountNumber());
        assertEquals(BigDecimal.ZERO, accountResponse.amount());
        verify(accountRepository).save(any(Account.class));
    }
    @Test
    void createAccountShouldThrowExceptionWhenUserNotFound() {
        AccountRequest request = new AccountRequest("12345", "notfound@mail.com");
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(request));
    }

    @Test
    void depositShouldIncreaseBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse response = accountService.deposit(1L, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), response.amount());
        verify(accountRepository).save(account);
    }

    @Test
    void depositShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(1L, BigDecimal.valueOf(50)));
    }

    @Test
    void withDrawShouldDecreaseBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse response = accountService.withDraw(1L, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(50), response.amount());
    }

    @Test
    void withDrawShouldThrowExceptionWhenInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(30));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> accountService.withDraw(1L, BigDecimal.valueOf(50)));
    }

    @Test
    void withDrawShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.withDraw(1L, BigDecimal.valueOf(50)));
    }

    @Test
    void getAccountsByUserShouldReturnList() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);
        when(accountRepository.findAccountsByUser_Email("test@mail.com")).thenReturn(List.of(account));

        List<AccountResponse> response = accountService.getAccountsByUser("test@mail.com");

        assertEquals(1, response.size());
        assertEquals("12345", response.get(0).accountNumber());
    }

    @Test
    void getAccountsByUserShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.existsByEmail("notfound@mail.com")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> accountService.getAccountsByUser("notfound@mail.com"));
    }

    @Test
    void deleteByIdShouldReturnSuccessWhenAccountExists() {
        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).deleteById(1L);

        SimpleResponse response = accountService.deleteById(1L);

        assertEquals("счет успешно удален", response.message());
        verify(accountRepository).deleteById(1L);
    }

    @Test
    void deleteByIdShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteById(1L));
    }

    @Test
    void findTotalBalanceShouldReturnValue() {
        when(accountRepository.findTotalBalance()).thenReturn(BigDecimal.valueOf(15000));

        BigDecimal total = accountService.findTotalBalance();

        assertEquals(BigDecimal.valueOf(15000), total);
        verify(accountRepository).findTotalBalance();
    }



}
