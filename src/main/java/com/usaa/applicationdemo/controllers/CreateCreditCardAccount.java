package com.usaa.applicationdemo.controllers;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.usaa.applicationdemo.entities.CreditCardAccount;
import com.usaa.applicationdemo.entities.Customer;
import com.usaa.applicationdemo.repositories.CreditCardAccountRepository;
import com.usaa.applicationdemo.repositories.CustomerRepository;

@RestController
public class CreateCreditCardAccount {
  @Autowired
  CreditCardAccountRepository creditCardRepository;

  @Autowired
  CustomerRepository customerRepository;

  @PostMapping("/customers/{customer_id}/creditcardaccounts")
  ResponseEntity<SuccessfulResponse> createCreditCardAccount(
      @PathVariable("customer_id") long customer_id,
      @Valid @RequestBody CreditCardAccount newCreditCardAccount) {

    Customer customer = customerRepository.findById(customer_id).orElseThrow(() -> {
      String message = String.format("The customer with id %s could not be found", customer_id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    });

    newCreditCardAccount.setCustomer(customer);
    newCreditCardAccount.loadCredit();

    creditCardRepository.save(newCreditCardAccount);

    return ResponseEntity.ok(new SuccessfulResponse(newCreditCardAccount));
  }

  class SuccessfulResponse {
    public String type = "CreditCardAccountCreated";
    public long credit_card_account_id;
    public Customer customer;
    public long availableCredit;
    public long creditLimit;
    public HashMap<String, String> links = new HashMap<String, String>();

    public SuccessfulResponse(CreditCardAccount account) {
      this.credit_card_account_id = account.getId();
      this.customer = account.getCustomer();
      this.availableCredit = account.getCreditLimit();
      this.creditLimit = account.getAvailableCredit();
      this.links.put("AddTransaction",
          String.format("/creditcardaccounts/%s/transactions", this.credit_card_account_id));
    }
  }
}
