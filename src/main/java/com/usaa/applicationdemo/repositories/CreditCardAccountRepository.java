package com.usaa.applicationdemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usaa.applicationdemo.entities.CreditCardAccount;

public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, Long> {
  List<CreditCardAccount> findByCustomerId(long customer_id);
}
