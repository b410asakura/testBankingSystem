package com.example.banking.service;

import com.example.banking.dto.SimpleResponse;
import com.example.banking.dto.userDto.UserAccountResponse;
import com.example.banking.dto.userDto.UserRequest;
import com.example.banking.dto.userDto.UserResponse;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserRequest userRequest);

    SimpleResponse deleteById(Long id);

    List<UserAccountResponse> getAllUsersAboveAmount(BigDecimal amount);

    UserResponse findUserByEmail(String email);

}
