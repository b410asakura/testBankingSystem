package com.example.banking.service;

import com.example.banking.dto.response.UserAccountResponse;
import com.example.banking.dto.request.UserRequest;
import com.example.banking.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest userRequest);

    ResponseEntity<Void> deleteById(Long id);

    List<UserAccountResponse> getAllUsersAboveAmount(BigDecimal amount);

    UserResponse findUserByEmail(String email);

}
