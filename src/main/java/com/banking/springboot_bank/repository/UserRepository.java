package com.banking.springboot_bank.repository;

import com.banking.springboot_bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //to check if this details exist
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);

}
