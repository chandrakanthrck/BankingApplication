package com.banking.springboot_bank.repository;

import com.banking.springboot_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
