package com.example.banking.service.impl;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.userDto.UserAccountResponse;
import com.example.banking.dto.userDto.UserRequest;
import com.example.banking.dto.userDto.UserResponse;
import com.example.banking.entity.User;
import com.example.banking.exception.DuplicateEmailException;
import com.example.banking.exception.UserNotFoundException;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Попытка создать пользователя с email: {}", userRequest.email());
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            log.warn("Попытка создать пользователя с уже существующим email: {}", userRequest.email());
            throw new DuplicateEmailException(userRequest.email());
        }

        User user = new User();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setRegistrationDate(LocalDate.now());
        User saved = userRepository.save(user);
        log.info("Пользователь {} успешно сохранён в базе с id {}", saved.getName(), saved.getId());
        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRegistrationDate()
        );
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("Попытка получения пользователя с id {}", id);
        User user = findUserById(id);
        log.info("Пользователь с id {} успешно получен", id);
        return mapToResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .registrationDate(user.getRegistrationDate())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Попытка получения всех пользователей");
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            log.info("Список пользователей пуст");
            throw new RuntimeException("Список пользователей пуст");
        }
        return allUsers
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRegistrationDate())).
                toList();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        log.info("Попытка обновления данных пользователя c id {}", id);
        User user = findUserById(id);
        if (!user.getEmail().equals(userRequest.email()) &&
                userRepository.findByEmail(userRequest.email()).isPresent()) {
            log.info("Почта с таким именем уже существует");
            throw new DuplicateEmailException(userRequest.email());
        }
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());

        User updated = userRepository.save(user);
        log.info("Данные пользователя с id {} успешно обновлены", id);
        return mapToResponse(updated);
    }

    @Override
    public SimpleResponse deleteById(Long id) {
        log.info("Попытка удаления пользователя с id {}", id);
        User user = findUserById(id);
        userRepository.delete(user);
        log.info("Пользователь с id {} успешно удален", id);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("user with id %s was deleted", id))
                .build();
    }

    @Override
    public List<UserAccountResponse> getAllUsersAboveAmount(BigDecimal amount) {
        log.info("Попытка получения пользователей с счетом выше {}", amount);
        List<UserAccountResponse> users = userRepository.findUsersWithAllAccountsAbove(amount);
        if (users.isEmpty()) {
            log.info("Списко пользователей пуст");
            throw new UserNotFoundException(amount);
        }
        return users.stream()
                .map(u -> new UserAccountResponse(
                                u.id(),
                                u.name(),
                                u.email(),
                                u.registrationDate(),
                                u.accountNumber(),
                                u.amount()
                        )
                ).toList();
    }

    @Override
    public UserResponse findUserByEmail(String email) {
        log.info("Попытка получения пользователя по email {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.info("Пользователь не найден с email {}", email);
                    return new UserNotFoundException(email);
                }
        );
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .registrationDate(user.getRegistrationDate())
                .build();
    }




    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Пользователь не найден с id {}", id);
                    return new UserNotFoundException(id);
                });
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRegistrationDate());
    }

}
