package com.usaa.applicationdemo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usaa.applicationdemo.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  List<Transaction> findByCreditCardAccountId(long account_id);
}
