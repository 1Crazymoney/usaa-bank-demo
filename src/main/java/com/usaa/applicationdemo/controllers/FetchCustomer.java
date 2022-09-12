package com.usaa.applicationdemo.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.usaa.applicationdemo.entities.CreditCardAccount;
import com.usaa.applicationdemo.entities.Customer;
import com.usaa.applicationdemo.repositories.CreditCardAccountRepository;
import com.usaa.applicationdemo.repositories.CustomerRepository;

@RestController
public class FetchCustomer {
  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CreditCardAccountRepository creditCardAccountRepository;

  @GetMapping("/")
  public ResponseEntity<String> helloUSAA() {
    return ResponseEntity.ok("Hello, USAA");
  }

  @GetMapping("/customers/{customer_id}")
  public ResponseEntity<SuccessfulResponse> fetchCustomer(@PathVariable("customer_id") long customer_id) {
    var customer = customerRepository.findById(customer_id).orElseThrow(() -> {
      String message = String.format("The customer with id %s could not be found", customer_id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    });

    List<CreditCardAccount> accounts = creditCardAccountRepository.findByCustomerId(customer.getId());

    List<CreditCardAccountDTO> accountDTOs = accounts.stream().map(account -> {
      return new CreditCardAccountDTO(account);
    }).collect(Collectors.toList());

    return ResponseEntity.ok(new SuccessfulResponse(customer, accountDTOs));
  }

  class SuccessfulResponse {
    public long customer_id;
    public String name;
    public List<CreditCardAccountDTO> accounts;

    SuccessfulResponse(Customer customer, List<CreditCardAccountDTO> accounts) {
      this.customer_id = customer.getId();
      this.name = customer.getName();
      this.accounts = accounts;
    }
  }

  class CreditCardAccountDTO {
    public long account_id;
    public long availableCredit;
    public long creditLimit;

    CreditCardAccountDTO(CreditCardAccount account) {
      this.account_id = account.getId();
      this.availableCredit = account.getAvailableCredit();
      this.creditLimit = account.getCreditLimit();
    }
  }
}
