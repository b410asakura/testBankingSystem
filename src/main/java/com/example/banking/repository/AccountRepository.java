package com.example.banking.repository;

import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByUser(User user);

    List<Account> findAccountsByUser_Email(String userEmail);

    @Query("""
                    SELECT sum(a.balance) FROM Account a
            """)
    BigDecimal findTotalBalance();
}
