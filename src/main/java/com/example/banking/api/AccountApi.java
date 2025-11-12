package com.example.banking.api;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.accountDto.AccountRequest;
import com.example.banking.dto.accountDto.AccountResponse;
import com.example.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "account API")
@Slf4j
public class AccountApi {
    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Метод для создания нового счета")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        log.info("Запрос на создание счета");
        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(accountRequest.accountNumber())
                .toUri();
        log.info("Был создан счет для пользователя {}", accountRequest.userEmail());
        return ResponseEntity.created(location).body(accountResponse);
    }

    @PatchMapping("/deposit/{id}")
    @Operation(summary = "Метод для пополнения счета")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        log.info("Запрос на пополнение счета");
        AccountResponse accountResponse = accountService.deposit(id, amount);
        log.info("Счет был пополнен на сумму {}", amount);
        return ResponseEntity.ok(accountResponse);
    }

    @PatchMapping("/withDraw/{id}")
    @Operation(summary = "Метод для снятия счета")
    public ResponseEntity<AccountResponse> withDraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount
    ) {
        log.info("Запрос на снятие с счета");
        AccountResponse accountResponse = accountService.withDraw(id, amount);
        log.info("C счета было снято {}", amount);
        return ResponseEntity.ok(accountResponse);
    }

    @GetMapping
    @Operation(summary = "Метод для получения счетов по email")
    public ResponseEntity<List<AccountResponse>> getAccountsByEmail(@RequestParam String userEmail) {
        log.info("Запрос на получение счетов по email {}", userEmail);
        List<AccountResponse> accountsByUser = accountService.getAccountsByUser(userEmail);
        if (accountsByUser.isEmpty()) {
            log.info("По данному email не найдено счетов");
            return ResponseEntity.notFound().build();
        }
        log.info("Получены счета по email: {}", accountsByUser);
        return ResponseEntity.ok(accountsByUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Метод для удаления пользователя")
    public ResponseEntity<SimpleResponse> deleteAccountsByEmail(@PathVariable Long id) {
        log.info("Запрос на удаление счета");
        SimpleResponse simpleResponse = accountService.deleteById(id);
        log.info("Счет с id {} был удален", id);
        return ResponseEntity.ok(simpleResponse);
    }

    @GetMapping("/total-balance")
    @Operation(summary = "Метод для получения общей суммы в банке")
    public BigDecimal getTotalBalance() {
        log.info("Запрос на получение средств в банке");
        BigDecimal totalBalance = accountService.findTotalBalance();
        log.info("Общая сумма в банке {}", totalBalance);
        return totalBalance;
    }
}

