package com.example.banking.api;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.userDto.UserAccountResponse;
import com.example.banking.dto.userDto.UserRequest;
import com.example.banking.dto.userDto.UserResponse;
import com.example.banking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "user API")
@Slf4j
public class UserApi {
    private final UserService userService;


    @PostMapping
    @Operation(summary = "Метод для создания новых пользователей")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Запрос на создание пользователя с email: {}", userRequest.email());
        UserResponse userResponse = userService.createUser(userRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponse.id())
                .toUri();
        log.info("Пользователь {} создан, URI {}", userResponse.id(), location);
        return ResponseEntity.created(location).body(userResponse); // 201 Created
    }

    @GetMapping("/{id}")
    @Operation(summary = "Метод для получения пользователя по id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя с id: {} ", id);
        UserResponse user = userService.getUserById(id);
        log.info("Получен пользователь с id {}", id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    @Operation(summary = "Метод для получения пользователя по email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        log.info("Запрос на получение пользователя с email: {} ", email);
        UserResponse user = userService.findUserByEmail(email);
        log.info("Получен пользователь с email {}", email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/")
    @Operation(summary = "Метод для получения всех пользователей")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Запрос на полчение всех пользователей");
        List<UserResponse> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.warn("Список пользователей пуст");
            return ResponseEntity.noContent().build();
        }
        log.info("Получены все пользователи");
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Метод для обновления пользователя")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Запрос на обновление пользователя с id {}", id);
        UserResponse userResponse = userService.updateUser(id, userRequest);
        log.info("Пользователь с id {} обновлен", id);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Метод для удаления пользователя")
    public ResponseEntity<SimpleResponse> deleteUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя с id {}", id);
        SimpleResponse simpleResponse = userService.deleteById(id);
        log.info("Пользователь с id {} был удален", id);
        return ResponseEntity.ok(simpleResponse);
    }

    @GetMapping("/above")
    @Operation(summary = "Метод для получения пользователей с балансом выше ")
    public ResponseEntity<List<UserAccountResponse>> getUsersAbove(@RequestParam BigDecimal amount) {
        log.info("Запрос на получение пользователей с балансом выше {}", amount);
        List<UserAccountResponse> allUsersAboveAmount = userService.getAllUsersAboveAmount(amount);
        log.info("Пользователи с балансом выше {} получены", amount);
        return ResponseEntity.ok(allUsersAboveAmount);
    }

}
