package com.example.banking.repository;

import com.example.banking.dto.userDto.UserAccountResponse;
import com.example.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
                SELECT new com.example.banking.dto.userDto.UserAccountResponse(
                    u.id, u.name, u.email, u.registrationDate, a.accountNumber, a.balance
                )
                FROM User u
                JOIN u.accounts a
                WHERE a.balance > :minBalance
            """)
    List<UserAccountResponse> findUsersWithAllAccountsAbove(@Param("minBalance") BigDecimal minBalance);

    @Query("""
                        SELECT u FROM User u WHERE u.email = :email
            """)
    Optional<User> findUsersWithEmail(@Param("email") String email);


}
