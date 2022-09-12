package com.usaa.applicationdemo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.usaa.applicationdemo.entities.CreditCardAccount;
import com.usaa.applicationdemo.entities.Transaction;
import com.usaa.applicationdemo.repositories.CreditCardAccountRepository;
import com.usaa.applicationdemo.repositories.CustomerRepository;
import com.usaa.applicationdemo.repositories.TransactionRepository;

@RestController
public class FetchCustomerTransactions {
  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CreditCardAccountRepository creditCardAccountRepository;

  @Autowired
  TransactionRepository transactionRepository;

  @GetMapping("/creditcardaccounts/{account_id}/transactions")
  public ResponseEntity<SuccessfulResponse> fetchCustomerTransactions(@PathVariable("account_id") long account_id) {
    var account = creditCardAccountRepository.findById(account_id).orElseThrow(() -> {
      String message = String.format("The account with id %s could not be found", account_id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    });

    var transactions = transactionRepository.findByCreditCardAccountId(account.getId());

    return ResponseEntity.ok(new SuccessfulResponse(account, transactions));
  }

  class SuccessfulResponse {
    public long customer_id;
    public String customer_name;
    public long account_id;
    public long availableCreditRemaining;
    public long creditLimit;
    public List<Transaction> transactions;

    SuccessfulResponse(CreditCardAccount account, List<Transaction> transactions) {
      this.customer_id = account.getCustomer().getId();
      this.customer_name = account.getCustomer().getName();
      this.account_id = account.getId();
      this.availableCreditRemaining = account.getAvailableCredit();
      this.creditLimit = account.getCreditLimit();
      this.transactions = transactions;
    }
  }
}
