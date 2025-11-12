package com.example.banking.service.impl;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.accountDto.AccountRequest;
import com.example.banking.dto.accountDto.AccountResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import com.example.banking.exception.AccountNotFoundException;
import com.example.banking.exception.UserNotFoundException;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {
        log.info("Попытка создать счет для пользователя с email {}", accountRequest.userEmail());
        Account account = new Account();
        account.setAccountNumber(accountRequest.accountNumber());
        account.setBalance(BigDecimal.ZERO);
        User user = userRepository.findByEmail(accountRequest.userEmail()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + accountRequest.userEmail())
        );
        account.setUser(user);
        log.info("Счет был создан");
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponse deposit(Long id, BigDecimal amount) {
        log.info("Попытка пополнения счета");
        Account account = findById(id);
        account.setBalance(account.getBalance().add(amount));
        log.info("счет был пополнен на сумму {}", amount);
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponse withDraw(Long id, BigDecimal amount) {
        log.info("Попытка снятия с счета");
        Account account = findById(id);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        log.info("с счета была снята сумма {}", amount);
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    public List<AccountResponse> getAccountsByUser(String userEmail) {
        log.info("Попытка получения счетов пользователя с email {}", userEmail);
        if (!userRepository.existsByEmail(userEmail)) {
            throw new UserNotFoundException(userEmail);
        }
        log.info("Получены счета пользователя с email {}", userEmail);
        return accountRepository.findAccountsByUser_Email(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SimpleResponse deleteById(Long id) {
        log.info("Попытка удаления счета");
        accountRepository.findById(id).orElseThrow(
                () -> {
                    log.info("Счет с таким id: {} не найден", id);
                    return new AccountNotFoundException("Account not found with id: " + id);
                }
        );
        accountRepository.deleteById(id);
        log.info("Счет с id: {} успешно удален ", id);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("счет успешно удален")
                .build();
    }

    @Override
    public BigDecimal findTotalBalance() {
        log.info("Попытка получения суммы всех средств в системе");
        BigDecimal totalBalance = accountRepository.findTotalBalance();
        log.info("Общая сумма средств в системе {}", totalBalance);
        return totalBalance;
    }

    private Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(account.getId(), account.getAccountNumber(), account.getBalance());
    }
}
