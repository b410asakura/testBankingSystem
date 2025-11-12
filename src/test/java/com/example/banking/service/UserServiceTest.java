package com.example.banking.service;

import com.example.banking.dto.userDto.UserAccountResponse;
import com.example.banking.dto.userDto.UserRequest;
import com.example.banking.dto.userDto.UserResponse;
import com.example.banking.entity.User;
import com.example.banking.exception.DuplicateEmailException;
import com.example.banking.exception.UserNotFoundException;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
   public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserSuccess() {
        UserRequest request = new UserRequest("John Doe", "john@mail.com");
        User user = new User();
        user.setId(1L);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRegistrationDate(LocalDate.now());

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@mail.com", response.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_throwsException() {
        UserRequest request = new UserRequest("John Doe", "john@mail.com");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
    }

    @Test
    void getUserById_success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@mail.com");
        user.setRegistrationDate(LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertEquals("Alice", response.name());
        assertEquals("alice@mail.com", response.email());
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_success() {
        UserRequest request = new UserRequest("Bob Updated", "bob@mail.com");
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@mail.com");
        user.setRegistrationDate(LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertEquals("Bob Updated", response.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_duplicateEmail_throwsException() {
        UserRequest request = new UserRequest("Bob Updated", "existing@mail.com");
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("existing@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void getAllUsersAboveAmount_success() {
        UserAccountResponse userAccount = new UserAccountResponse(
                1L, "John", "john@mail.com", LocalDate.now(), "12345", BigDecimal.valueOf(20000)
        );
        when(userRepository.findUsersWithAllAccountsAbove(BigDecimal.valueOf(10000)))
                .thenReturn(List.of(userAccount));

        List<UserAccountResponse> result = userService.getAllUsersAboveAmount(BigDecimal.valueOf(10000));

        assertEquals(1, result.size());
        assertEquals("12345", result.get(0).accountNumber());
    }

    @Test
    void getAllUsersAboveAmount_empty_throwsException() {
        when(userRepository.findUsersWithAllAccountsAbove(BigDecimal.valueOf(10000)))
                .thenReturn(List.of());

        assertThrows(UserNotFoundException.class,
                () -> userService.getAllUsersAboveAmount(BigDecimal.valueOf(10000)));
    }


}
